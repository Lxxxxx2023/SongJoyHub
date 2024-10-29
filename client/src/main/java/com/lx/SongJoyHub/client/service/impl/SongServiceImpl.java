package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.constant.SystemConstant;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.service.SongService;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 歌曲业务实现层
 */
@Service
@RequiredArgsConstructor
public class SongServiceImpl extends ServiceImpl<SongMapper, SongDO> implements SongService {

    private final ChainHandlerContext chainHandlerContext;

    private final SongMapper songMapper;

    private final RedissonClient redissonClient;

    private final SongReviewMapper songReviewMapper;

    private final ThreadPoolExecutor analysisDurationThreadPool = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() << 1,
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMusic(MusicCreateReqDTO requestParam) {
        // 通过责任链检验参数
        chainHandlerContext.handler(ChainBizMarkEnum.MUSIC_CREATE_KEY.name(),requestParam);
        // 插入数据库
        SongDO songDO = null;
        try {
            songDO = BeanUtil.toBean(requestParam, SongDO.class);
            songMapper.insert(songDO);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("该歌词者已有该歌曲名");
        }
        // 创建审批任务
        SongReviewDO songReviewDO = SongReviewDO.builder()
                .songId(songDO.getSongId())
                .submitterId(requestParam.getUserId())
                .build();
        songReviewMapper.insert(songReviewDO);
    }
}
