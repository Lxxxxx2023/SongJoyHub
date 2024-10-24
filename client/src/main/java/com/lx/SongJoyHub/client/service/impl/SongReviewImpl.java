package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.lx.SongJoyHub.client.service.SongReviewService;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 歌曲生审批业务实现层
 */
@Service
@RequiredArgsConstructor
public class SongReviewImpl extends ServiceImpl<SongReviewMapper, SongReviewDO> implements SongReviewService {

    private final SongReviewMapper songReviewMapper;

    private final SongMapper songMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void songExamine(SongReviewReqDTO requestParam) {
        SongReviewDO songReviewDO = songReviewMapper.selectById(requestParam.getId());
        if (songReviewDO == null) {
            throw new ServiceException("该审批信息不存在");
        }
        if (songReviewDO.getStatus() != 0) {
            throw new ServiceException("本次审批已经已经完成，请不要重复操作");
        }
        songReviewDO.setExamineTime(new Date());
        songReviewDO.setNotes(requestParam.getNotes());
        songReviewDO.setStatus(requestParam.getStatus());
        songReviewDO.setOpId(requestParam.getOpId());
        updateById(songReviewDO);

        // 更新歌曲状态
        if (requestParam.getStatus() == 1) {
            LambdaUpdateWrapper<SongDO> updateWrapper = Wrappers.lambdaUpdate(SongDO.class)
                    .eq(SongDO::getSongId, songReviewDO.getSongId())
                    .eq(SongDO::getDelFlag, 0);
            SongDO songDO = SongDO.builder()
                    .status(1) //可播放
                    .build();
            int update = songMapper.update(songDO, updateWrapper);
            if (!SqlHelper.retBool(update)) {
                throw new ServiceException("审批时更新歌曲状态失败");
            }
            // 缓存预热 TODO 感觉有点怪
            LambdaQueryWrapper<SongDO> queryWrapper = Wrappers.lambdaQuery(SongDO.class)
                    .eq(SongDO::getSongId, songReviewDO.getSongId())
                    .eq(SongDO::getDelFlag, 0)
                    .eq(SongDO::getStatus, 1);
            SongQueryRespDTO songQueryRespDTO = BeanUtil.toBean(songMapper.selectOne(queryWrapper), SongQueryRespDTO.class);
            Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(songQueryRespDTO, false, true);
            Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                    entry -> entry.getValue() != null ? entry.getValue().toString() : ""));

            String songCacheKey = String.format(RedisConstant.SONG_KEY, songDO.getSongId());
            String luaScript = "redis.call('HMSET',KEYS[1],unpack(ARGV, 1, #ARGV - 1)" +
                    "redis.call('EXPIREAT',KEYS[1],ARGV[#ARGV])";
            List<String> keys = Collections.singletonList(songCacheKey);
            List<String> args = new ArrayList<>(actualCacheTargetMap.size() * 2 + 1);
            actualCacheTargetMap.forEach((k, v) -> {
                args.add(k);
                args.add(v);
            });
            args.add(String.valueOf(100000L));
            //执行lua脚本
            stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), keys, args);
        }

    }
}
