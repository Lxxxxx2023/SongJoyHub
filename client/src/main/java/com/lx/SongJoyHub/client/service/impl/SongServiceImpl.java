package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.common.enums.ReviewTypeEnum;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicDeleteReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicUpdateReqDTO;
import com.lx.SongJoyHub.client.service.SongService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMusic(MusicCreateReqDTO requestParam) {
        // 通过责任链检验参数
        chainHandlerContext.handler(ChainBizMarkEnum.MUSIC_CREATE_KEY.name(),requestParam);
        SongDO songDO = SongDO.builder()
                    .category(requestParam.getCategory())
                    .lyric(requestParam.getLyric())
                    .introduction(requestParam.getDesc())
                    .songAddress(requestParam.getSongAddress())
                    .duration(requestParam.getDuration())
                    .songName(requestParam.getSongName())
                    .singer(requestParam.getSinger())
                    .songLanguage(requestParam.getSongLanguage())
                    .songStatus(0) // 正在审核
                    .build();
        // 创建审批任务
        String songTOJson = JSON.toJSONString(songDO);
        SongReviewDO songReviewDO = SongReviewDO.builder()
//                .committerId(Long.valueOf(UserContext.getUserId())
                .committerId(1L)
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .nowData(songTOJson)
                .status(0)
                .type(ReviewTypeEnum.INSERT.getCode())
                .cause("录入新歌曲")
                .build();
        songReviewMapper.insert(songReviewDO);
    }

    @Override
    public void deleteMusic(MusicDeleteReqDTO requestParam) {
        // 创建审批任务
        SongDO songDO = SongDO.builder()
                .songId(requestParam.getSongId())
                .delFlag(1)
                .build();
        SongReviewDO songReviewDO = SongReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .nowData(JSON.toJSONString(songDO))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.DELETE.getCode())
                .build();
        songReviewMapper.insert(songReviewDO);
    }

    @Override
    public void updateMusic(MusicUpdateReqDTO requestParam) {
        LambdaQueryWrapper<SongDO> queryWrapper = Wrappers.lambdaQuery(SongDO.class)
                .eq(SongDO::getSongId, requestParam.getSongId())
                .eq(SongDO::getDelFlag, 0)
                .eq(SongDO::getSongStatus,1);
        SongDO oldSongDO = songMapper.selectOne(queryWrapper);
        if (oldSongDO == null) {
            throw new ServiceException("该歌曲不存在，怎么进行修改呢？");
        }
        SongDO newSongDO = BeanUtil.toBean(requestParam, SongDO.class);
        SongReviewDO songReviewDO = SongReviewDO.builder()
//                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerId(1L)
//                .committerName(UserContext.getUser().getUserName()
                .committerName("lx")
                .originalData(JSON.toJSONString(oldSongDO))
                .nowData(JSON.toJSONString(newSongDO))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.UPDATE.getCode())
                .build();
        songReviewMapper.insert(songReviewDO);
    }
}
