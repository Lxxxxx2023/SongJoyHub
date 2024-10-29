package com.lx.SongJoyHub.client.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewRespDTO;
import com.lx.SongJoyHub.client.service.SongReviewService;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 歌曲生审批业务实现层
 */
@Service
@RequiredArgsConstructor
public class SongReviewServiceImpl extends ServiceImpl<SongReviewMapper, SongReviewDO> implements SongReviewService {

    private final SongReviewMapper songReviewMapper;

    private final SongMapper songMapper;

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public List<SongReviewRespDTO> examineQueryUnprocessed() {
        LambdaQueryWrapper<SongReviewDO> queryWrapper = Wrappers.lambdaQuery(SongReviewDO.class)
                .eq(SongReviewDO::getStatus, 0); // 查询未处理的
        List<SongReviewDO> songReviewDOS = songReviewMapper.selectList(queryWrapper);
        return songReviewDOS.stream().map(each -> BeanUtil.toBean(each, SongReviewRespDTO.class)).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineSaveMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        LambdaUpdateWrapper<SongReviewDO> updateWrapper = Wrappers.lambdaUpdate(SongReviewDO.class)
                .eq(SongReviewDO::getId,requestParam.getId())
//                .set(SongReviewDO::getOpId, UserContext.getUserId())
//                .set(SongReviewDO::getOpName,UserContext.getUser().getUserName())
                .set(SongReviewDO::getStatus, requestParam.getStatus())
                .set(SongReviewDO::getNotes,requestParam.getNotes());
        int update = songReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批失败 id: " + requestParam.getId());
        }
        if(requestParam.getStatus() != 1) return;
        // 审核通过 歌曲入库
        SongDO songDO = JSON.parseObject(requestParam.getNowData(), SongDO.class);
        songDO.setSongStatus(1);
        try{
            songMapper.insert(songDO);
        }catch (DuplicateKeyException e) {
            throw new ServiceException("该歌手的该歌曲已存在");
        }
        // 创建缓存
        refactorSongCache(songDO);
    }



    @Override
    public void examineDeleteMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        LambdaUpdateWrapper<SongReviewDO> updateWrapper = Wrappers.lambdaUpdate(SongReviewDO.class)
                .eq(SongReviewDO::getId, requestParam.getId())
                .set(SongReviewDO::getStatus, requestParam.getStatus())
                .set(SongReviewDO::getNotes,requestParam.getNotes());
        int update = songReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批删除歌曲失败 id: " + requestParam.getId());
        }
        if(requestParam.getStatus() != 1) return;
        SongDO songDO = JSON.parseObject(requestParam.getNowData(), SongDO.class);
        LambdaUpdateWrapper<SongDO> updateWrapperSong = Wrappers.lambdaUpdate(SongDO.class)
                .eq(SongDO::getSongId, songDO.getSongId())
                .set(SongDO::getDelFlag,1); // 标记为删除
        songMapper.update(updateWrapperSong);
        stringRedisTemplate.delete(String.format(RedisConstant.SONG_KEY, songDO.getSongId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineUpdateMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        LambdaUpdateWrapper<SongReviewDO> updateWrapper = Wrappers.lambdaUpdate(SongReviewDO.class)
                .eq(SongReviewDO::getId, requestParam.getId())
                .set(SongReviewDO::getStatus, requestParam.getStatus())
                .set(SongReviewDO::getNotes,requestParam.getNotes());
        int update = songReviewMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批修改歌曲失败 id: " + requestParam.getId());
        }
        if(requestParam.getStatus() != 1) return;
        SongDO songDO = JSON.parseObject(requestParam.getNowData(), SongDO.class);
        int updateSong = songMapper.updateSong(songDO);
        if(!SqlHelper.retBool(updateSong)) {
            throw new ServiceException("审核歌曲修改 id: " + requestParam.getId());
        }
        SongDO newSong = songMapper.selectById(songDO.getSongId());
        // 重构缓存
        refactorSongCache(newSong);
    }

    private void refactorSongCache(SongDO songDO) {
        //存入缓存中
        SongQueryRespDTO songQueryRespDTO = BeanUtil.toBean(songDO, SongQueryRespDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(songQueryRespDTO, false, true);
        Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue() != null ? entry.getValue().toString() : ""));

        String songCacheKey = String.format(RedisConstant.SONG_KEY, songDO.getSongId());
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
