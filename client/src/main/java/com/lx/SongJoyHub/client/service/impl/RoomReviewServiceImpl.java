package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.RoomStatusEnum;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dao.entity.UpdateRoomReviewBO;
import com.lx.SongJoyHub.client.dao.mapper.RoomMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReviewMapper;
import com.lx.SongJoyHub.client.dto.req.RoomReviewMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewQueryDiffRespDTO;
import com.lx.SongJoyHub.client.service.RoomReviewService;
import com.lx.SongJoyHub.client.util.RedisUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;




/**
 * 房间审核业务逻辑实现层
 */
@Service
@RequiredArgsConstructor
public class RoomReviewServiceImpl extends ServiceImpl<RoomReviewMapper, RoomReviewDO> implements RoomReviewService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RoomReviewMapper roomReviewMapper;

    private final RoomMapper roomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineSaveRoom(RoomReviewReqDTO requestParam) {
        UpdateRoomReviewBO updateRoomReviewBO = getUpdateRoomReviewBO(requestParam);
        int update = roomReviewMapper.updateRoomReview(updateRoomReviewBO);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核创建房间失败");
        }
        if(requestParam.getStatus() != 1) return;
        // 新建房间信息
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
        roomDO.setRoomStatus(RoomStatusEnum.VIABLE.getCode());
        try{
            roomMapper.insert(roomDO);
        }catch (DuplicateKeyException e) {
            throw new ServiceException("该房间名已存在");
        }
        // 创建缓存
        refactorRoomCache(roomDO);
    }

    @Override
    @Transactional
    public void examineUpdateRoom(RoomReviewReqDTO requestParam) {
        UpdateRoomReviewBO updateRoomReviewBO = getUpdateRoomReviewBO(requestParam);
        int update = roomReviewMapper.updateRoomReview(updateRoomReviewBO);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核更新房间失败");
        }
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
        if(requestParam.getStatus() != 1) {
            int i = roomMapper.restoreRoomStatus(roomDO.getRoomId());
            if(!SqlHelper.retBool(i)){
                throw new ServiceException("房间状态已被恢复");
            }
            return;
        }
        int i = roomMapper.updateRoom(roomDO);
        if(!SqlHelper.retBool(i)){
            throw new ServiceException("审核更新房间失败");
        }
        // 重构缓存
        RoomDO nowRoomDO = roomMapper.selectById(roomDO.getRoomId());
        refactorRoomCache(nowRoomDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineDeleteRoom(RoomReviewReqDTO requestParam) {
        UpdateRoomReviewBO updateRoomReviewBO = getUpdateRoomReviewBO(requestParam);
        int update = roomReviewMapper.updateRoomReview(updateRoomReviewBO);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核删除房间失败");
        }
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
        if(requestParam.getStatus() != 1) {
            int i = roomMapper.restoreRoomStatus(roomDO.getRoomId());
            if(!SqlHelper.retBool(i)){
                throw new ServiceException("房间状态已被恢复");
            }
            return;
        }
        LambdaUpdateWrapper<RoomDO> deleteWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .eq(RoomDO::getRoomId,roomDO.getRoomId())
                .set(RoomDO::getRoomStatus,RoomStatusEnum.DELETED.getCode()); // 设为删除状态
        int i = roomMapper.update(deleteWrapper);
        if(!SqlHelper.retBool(i)){
            throw new ServiceException("审核删除房间失败");
        }
        stringRedisTemplate.delete(String.format(RedisConstant.ROOM_KEY,roomDO.getRoomId()));
    }

    @Override
    public List<RoomReviewPageQueryRespDTO> pageQueryRoomReview(Integer page, Integer pageSize) {
        return roomReviewMapper.pageQueryRoomReview(page - 1, pageSize);
    }

    @Override
    public RoomReviewQueryDiffRespDTO queryRoomReviewDiff(Integer id) {
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(id);
        RoomDO nowData = JSON.parseObject(roomReviewDO.getNowData(), RoomDO.class);
        RoomDO oldData = JSON.parseObject(roomReviewDO.getOriginalData(), RoomDO.class);
        return RoomReviewQueryDiffRespDTO.builder()
                .cause(roomReviewDO.getCause())
                .opName(roomReviewDO.getOpName())
                .committerName(roomReviewDO.getCommitterName())
                .createTime(roomReviewDO.getCreateTime())
                .oldData(oldData)
                .newData(nowData)
                .type(roomReviewDO.getType())
                .updateTime(roomReviewDO.getUpdateTime())
                .notes(roomReviewDO.getNotes())
                .build();
    }

    @Override
    public List<RoomReviewPageQueryRespDTO> multipleQueryRoomReview(RoomReviewMultipleQueryReqDTO requestParam) {
        requestParam.setPage(requestParam.getPage() - 1);
        return roomReviewMapper.multipleQueryRoomReview(requestParam);
    }

    private void refactorRoomCache(RoomDO roomDO) {
        //存入缓存中
        RoomQueryRespDTO roomQueryReqDTO = BeanUtil.toBean(roomDO, RoomQueryRespDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(roomQueryReqDTO, false, true);
        RedisUtil.convertHash(String.format(RedisConstant.ROOM_KEY,roomDO.getRoomId())
                ,stringRedisTemplate
                ,cacheTargetMap
                ,String.valueOf(Instant.now().getEpochSecond() + 100000)
        );
    }
    private static RoomDO getRoomDO(RoomReviewDO roomReviewDO) {
        return JSON.parseObject(roomReviewDO.getNowData(),RoomDO.class);
    }

    private static UpdateRoomReviewBO getUpdateRoomReviewBO(RoomReviewReqDTO requestParam) {
        return UpdateRoomReviewBO.builder()
                .roomReviewId(requestParam.getId())
                .opId(Long.valueOf(UserContext.getUserId()))
                .opName(UserContext.getUser().getUserName())
                .level(UserContext.getUser().getLevel())
                .notes(requestParam.getNotes())
                .status(requestParam.getStatus())
                .build();
    }
}
