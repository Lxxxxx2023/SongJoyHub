package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReservationDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.RoomMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReservationMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReviewMapper;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryAllRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryReviewRespDTO;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 房间业务逻辑实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomDO> implements RoomService {

    private final ChainHandlerContext chainHandlerContext;

    private final RoomMapper roomMapper;

    private final RoomReservationMapper roomReservationMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final RoomReviewMapper roomReviewMapper;

    @Override
    public void createRoom(RoomCreateReqDTO requestParam) {
        // 校验参数
        chainHandlerContext.handler(ChainBizMarkEnum.ROOM_CREATE_KEY.name(), requestParam);
        RoomDO roomDO = BeanUtil.toBean(requestParam, RoomDO.class);

        // 插入数据库
        try {
            roomMapper.insert(roomDO);
        } catch (DuplicateKeyException e) {
            log.error("房间名不能重复");
        }
    }

    @Override
    public void reservationRoom(RoomReservationReqDTO requestParam) {

        //查询缓存 是否存在冲突 时间上的冲突
        DateTime startTime = DateUtil.beginOfHour(requestParam.getStartTime());
        DateTime endTime = DateUtil.endOfHour(requestParam.getEndTime());
        String reservationRoomKey = String.format(RedisConstant.ROOM_RESERVATION_KEY, requestParam.getRoomId());
        String startTimeCache = String.valueOf(startTime.getTime());
        String endTimeCache = String.valueOf(endTime.getTime());
        Set<String> reservationRange = stringRedisTemplate.opsForZSet().rangeByScore(reservationRoomKey, startTime.getTime(), endTime.getTime());
        if (reservationRange != null && !reservationRange.isEmpty()) {
            throw new ServiceException("本房间预定的时间段发生冲突");
        }
        // 不存在时间范围冲突
        Long userId = Long.valueOf(UserContext.getUserId());
        RoomReservationDO roomReservationDO = RoomReservationDO.builder()
                .roomId(requestParam.getRoomId())
                .payAmount(requestParam.getPayAmount())
                .userId(userId)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        roomReservationMapper.insert(roomReservationDO);
        // 后续扣减用户余额 或者消耗某些物品
        // 更新缓存
        List<String> strings = Collections.singletonList(reservationRoomKey);
        List<String> times = List.of(userId.toString(), startTimeCache, userId.toString(), endTimeCache, String.valueOf(10000L));
        String luaScript = "redis.call('ZADD',KEYS[1],ARGV[1],ARGV[2])" +
                "redis.call('ZADD',KEYS[1],ARGV[3],ARGV[4])" +
                "redis.call('EXIREAT',KEYS[1],ARGV[5])";
        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), strings, times.toArray());
    }

    @Override
    public List<RoomQueryAllRespDTO> findRoom(RoomQueryReqDTO requestParam) {
        // TODO 检验用户不能访问未上线的房间或在维修的房间
        LambdaQueryWrapper<RoomDO> queryWrapper = Wrappers.lambdaQuery(RoomDO.class)
                .like(RoomDO::getRoomName, "%" + requestParam.getRoomName() + "%")
                .eq(RoomDO::getStatus, requestParam.getStatus())
                .eq(RoomDO::getType, requestParam.getType())
                .le(RoomDO::getPrice, requestParam.getMaxPrice())
                .gt(RoomDO::getPrice, requestParam.getMinPrice());
        List<RoomDO> roomDOS = roomMapper.selectList(queryWrapper);
        return roomDOS.stream().map(roomDO -> BeanUtil.toBean(roomDO, RoomQueryAllRespDTO.class)).toList();
    }

    @Override
    public void updateRoomInfo(RoomUpdateInfoReqDTO requestParam) {
        LambdaUpdateWrapper<RoomDO> updateWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .eq(RoomDO::getRoomId, requestParam.getRoomId());
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if(roomDO == null) {
            throw new ServiceException("该房间不存在 恶意请求");
        }
        RoomDO newRoom = BeanUtil.toBean(requestParam, RoomDO.class);
        int update = roomMapper.update(newRoom, updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("房间信息更新失败");
        }
    }

    @Override
    public void offLineRoom(RoomOffLineReqDTO requestParam) {
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if(roomDO == null) {
            throw new ServiceException("该房间下线失败：不存在该房间" + requestParam.getRoomId());
        }
        roomDO.setStatus(2);
        roomMapper.updateById(roomDO);
        // TODO 同时我们还需要记录这次是谁操作了
        // TODO 需要将预约了该房间的所以所有操作复原

    }

    @Override
    public void onLineRoom(RoomOnLineReqDTO requestParam) {
        LambdaUpdateWrapper<RoomDO> updateWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .set(RoomDO::getStatus,0)
                .eq(RoomDO::getRoomId, requestParam.getRoomId());
        int update = roomMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("上线房间失败，请检查房间号");
        }
        // TODO 记录操作日志
        // TODO 要重构缓存吗？
    }

    @Override
    public List<RoomQueryReviewRespDTO> findRoomReview(RoomQueryReviewReqDTO requestParam) {
        LambdaQueryWrapper<RoomReviewDO> queryWrapper = Wrappers.lambdaQuery(RoomReviewDO.class)
                .eq(RoomReviewDO::getRoomId, requestParam.getRoomId())
                .eq(RoomReviewDO::getStatus,requestParam.getStatus())
                .eq(RoomReviewDO::getOpId, requestParam.getOpId())
                .eq(RoomReviewDO::getType, requestParam.getType())
                .eq(RoomReviewDO::getSubmitterId, requestParam.getSubmitterId())
                .le(RoomReviewDO::getSubmitterName,requestParam.getMaxSubmitterTime())
                .ge(RoomReviewDO::getSubmitterTime,requestParam.getMinSubmitterTime())
                .le(RoomReviewDO::getSubmitterTime,requestParam.getMaxSubmitterTime())
                .ge(RoomReviewDO::getSubmitterTime,requestParam.getMinSubmitterTime());

        List<RoomReviewDO> roomReviewDOS = roomReviewMapper.selectList(queryWrapper);
        return roomReviewDOS.stream().map(roomReviewDO ->
                BeanUtil.toBean(roomReviewDO, RoomQueryReviewRespDTO.class)).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewRoom(RoomReviewReqDTO requestParam) {

        LambdaUpdateWrapper<RoomReviewDO> updateWrapper = Wrappers.lambdaUpdate(RoomReviewDO.class)
                .eq(RoomReviewDO::getId, requestParam.getId())
                .set(RoomReviewDO::getOpId, UserContext.getUserId())
                .set(RoomReviewDO::getNotes,requestParam.getNotes())
                .set(RoomReviewDO::getStatus,requestParam.getStatus());
        roomReviewMapper.update(updateWrapper);

        if(requestParam.getStatus() != 1) return;
        LambdaUpdateWrapper<RoomDO> roomWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .eq(RoomDO::getRoomId, requestParam.getRoomId())
                .set(RoomDO::getStatus,requestParam.getStatus());
        roomMapper.update(roomWrapper);

        // TODO 要引入缓存吗？

    }
}
