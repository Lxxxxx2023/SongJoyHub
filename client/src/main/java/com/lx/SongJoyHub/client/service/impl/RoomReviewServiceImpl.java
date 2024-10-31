package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.RoomMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReviewMapper;
import com.lx.SongJoyHub.client.dto.req.RoomQueryReviewReqDTO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;
import com.lx.SongJoyHub.client.service.RoomReviewService;
import com.lx.SongJoyHub.client.util.RedisUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
        LambdaUpdateWrapper<RoomReviewDO> updateWrapper = Wrappers.lambdaUpdate(RoomReviewDO.class)
                .eq(RoomReviewDO::getId,requestParam.getId())
                .set(RoomReviewDO::getNotes,requestParam.getNotes())
                .set(RoomReviewDO::getStatus,requestParam.getStatus());
        int update = roomReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核创建房间失败");
        }
        if(requestParam.getStatus() != 1) return;
        // 新建房间信息
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
        try{
            roomMapper.insert(roomDO);
        }catch (DuplicateKeyException e) {
            throw new ServiceException("该房间名已存在");
        }
        // 创建缓存
        refactorRoomCache(roomDO);
    }

    private static RoomDO getRoomDO(RoomReviewDO roomReviewDO) {
        JSONObject jsonObject = JSON.parseObject(roomReviewDO.getNowData());
        return RoomDO.builder()
                .price(jsonObject.getBigDecimal("price"))
                .roomId(Long.valueOf(jsonObject.getString("roomId")))
                .roomType(jsonObject.getString("roomType"))
                .roomName(jsonObject.getString("roomName"))
                .introduction(jsonObject.getString("introduction"))
                .build();
    }

    @Override
    @Transactional
    public void examineUpdateRoom(RoomReviewReqDTO requestParam) {
        LambdaUpdateWrapper<RoomReviewDO> updateWrapper = Wrappers.lambdaUpdate(RoomReviewDO.class)
                .eq(RoomReviewDO::getId,requestParam.getId())
                .set(RoomReviewDO::getNotes,requestParam.getNotes())
                .set(RoomReviewDO::getStatus,requestParam.getStatus());
        int update = roomReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核更新房间失败");
        }
        if(requestParam.getStatus() != 1) return;
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
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
        LambdaUpdateWrapper<RoomReviewDO> updateWrapper = Wrappers.lambdaUpdate(RoomReviewDO.class)
                .eq(RoomReviewDO::getId,requestParam.getId())
                .set(RoomReviewDO::getNotes,requestParam.getNotes())
                .set(RoomReviewDO::getStatus,requestParam.getStatus());
        int update = roomReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)){
            throw new ServiceException("审核删除房间失败");
        }
        if(requestParam.getStatus() != 1) return;
        RoomReviewDO roomReviewDO = roomReviewMapper.selectById(requestParam.getId());
        RoomDO roomDO = getRoomDO(roomReviewDO);
        LambdaUpdateWrapper<RoomDO> deleteWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .eq(RoomDO::getRoomId,roomDO.getRoomId())
                .set(RoomDO::getRoomStatus,3); // 设为删除状态
        int i = roomMapper.update(deleteWrapper);
        if(!SqlHelper.retBool(i)){
            throw new ServiceException("审核删除房间失败");
        }
        stringRedisTemplate.delete(String.format(RedisConstant.ROOM_KEY,roomDO.getRoomId()));
    }

    private void refactorRoomCache(RoomDO roomDO) {
        //存入缓存中
        RoomQueryReviewReqDTO roomQueryReviewReqDTO = BeanUtil.toBean(roomDO, RoomQueryReviewReqDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(roomQueryReviewReqDTO, false, true);
        RedisUtil.convertHash(String.format(RedisConstant.ROOM_KEY,roomDO.getRoomId())
                ,stringRedisTemplate
                ,cacheTargetMap
                ,String.valueOf(Instant.now().getEpochSecond() + 100000)
        );
    }
}
