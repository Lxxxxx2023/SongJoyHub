package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        RoomDO roomDO = JSON.parseObject(JSON.toJSONString(roomReviewDO.getNowData()), RoomDO.class);
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
        RoomDO roomDO = JSON.parseObject(roomReviewDO.getNowData(), RoomDO.class);
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
        RoomDO roomDO = JSON.parseObject(roomReviewDO.getNowData(), RoomDO.class);
        LambdaUpdateWrapper<RoomDO> deleteWrapper = Wrappers.lambdaUpdate(RoomDO.class)
                .eq(RoomDO::getRoomId,roomDO.getRoomId())
                .set(RoomDO::getRoomStatus,3); // 设为删除状态
        int i = roomMapper.updateRoom(roomDO);
        if(!SqlHelper.retBool(i)){
            throw new ServiceException("审核删除房间失败");
        }
        stringRedisTemplate.delete(String.format(RedisConstant.ROOM_KEY,roomDO.getRoomId()));
    }

    // TODO 后面进行重构
    private void refactorRoomCache(RoomDO roomDO) {
        //存入缓存中
        RoomQueryReviewReqDTO roomQueryReviewReqDTO = BeanUtil.toBean(roomDO, RoomQueryReviewReqDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(roomQueryReviewReqDTO, false, true);
        Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue() != null ? entry.getValue().toString() : ""));

        String songCacheKey = String.format(RedisConstant.ROOM_KEY, roomDO.getRoomId());
        String luaScript = "redis.call('HMSET',KEYS[1],unpack(ARGV, 1, #ARGV - 1))" +
                "redis.call('EXPIREAT',KEYS[1],ARGV[#ARGV])";
        List<String> keys = Collections.singletonList(songCacheKey);
        List<String> args = new ArrayList<>(actualCacheTargetMap.size() * 2 + 1);
        actualCacheTargetMap.forEach((k, v) -> {
            args.add(k);
            args.add(v);
        });
        args.add(String.valueOf(Instant.now().getEpochSecond() + 100000));
        //执行lua脚本
        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), keys, args.toArray());
    }
}
