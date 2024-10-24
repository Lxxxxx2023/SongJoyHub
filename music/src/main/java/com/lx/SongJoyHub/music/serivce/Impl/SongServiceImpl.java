package com.lx.SongJoyHub.music.serivce.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import com.lx.SongJoyHub.music.dao.entity.SongDO;
import com.lx.SongJoyHub.music.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.music.dao.mapper.SongMapper;
import com.lx.SongJoyHub.music.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.music.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.music.serivce.SongService;
import com.lx.SongJoyHub.music.serivce.basic.chain.MusicAbstractChainHandler;
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

    private final MusicAbstractChainHandler musicAbstractChainHandler;

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
        musicAbstractChainHandler.handler(requestParam);
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
