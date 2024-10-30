package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.common.enums.ReviewTypeEnum;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReservationDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.mapper.RoomMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReservationMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReviewMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
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
    private final SongMapper songMapper;

    @Override
    public void createRoom(RoomCreateReqDTO requestParam) {
        // 校验参数
        chainHandlerContext.handler(ChainBizMarkEnum.ROOM_CREATE_KEY.name(), requestParam);
        RoomDO roomDO = BeanUtil.toBean(requestParam, RoomDO.class);
        // 创建审核任务
        String roomJson = JSON.toJSONString(roomDO, SerializerFeature.WriteNonStringValueAsString);
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                .cause("新建房间")
//                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(roomJson)
                .type(ReviewTypeEnum.INSERT.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }

    @Override
    public void updateRoom(RoomUpdateReqDTO requestParam) {
        // 创建审核任务
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if (roomDO == null) {
            throw new ServiceException("该房间不存在 无法进行修改！");
        }
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                //                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(JSON.toJSONString((BeanUtil.toBean(requestParam,RoomDO.class)),SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(roomDO,SerializerFeature.WriteNonStringValueAsString))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.UPDATE.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }

    @Override
    public void deleteRoom(RoomDeleteReqDTO requestParam) {
        // 创建审核任务
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if (roomDO == null) {
            throw new ServiceException("该房间不存在 无法进行删除！");
        }
        RoomDO oldSongDO = BeanUtil.copyProperties(roomDO, RoomDO.class);
        roomDO.setRoomStatus(3); //删除
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
//                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(JSON.toJSONString(roomDO,SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(oldSongDO,SerializerFeature.WriteNonStringValueAsString))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.DELETE.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
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
}
