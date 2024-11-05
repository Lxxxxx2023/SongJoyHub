package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.common.enums.FlagEnum;
import com.lx.SongJoyHub.client.common.enums.ReviewTypeEnum;
import com.lx.SongJoyHub.client.common.enums.SongStatusEnum;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicDeleteReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicUpdateReqDTO;
import com.lx.SongJoyHub.client.dto.req.SongFuzzyInquiryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongQueryRespDTO;
import com.lx.SongJoyHub.client.service.SongService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import com.lx.SongJoyHub.framework.result.Result;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        chainHandlerContext.handler(ChainBizMarkEnum.MUSIC_CREATE_KEY.name(), requestParam);
        SongDO songDO = SongDO.builder()
                .category(requestParam.getCategory())
                .lyric(requestParam.getLyric())
                .introduction(requestParam.getIntroduction())
                .songAddress(requestParam.getSongAddress())
                .duration(requestParam.getDuration())
                .songName(requestParam.getSongName())
                .singer(requestParam.getSinger())
                .songLanguage(requestParam.getSongLanguage())
                .songStatus(SongStatusEnum.INREVIEW.getCode()) // 正在审核
                .build();
        // 创建审批任务
        String songTOJson = JSON.toJSONString(songDO);
        SongReviewDO songReviewDO = SongReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .nowData(songTOJson)
                .type(ReviewTypeEnum.INSERT.getCode())
                .cause(requestParam.getCause())
                .build();
        songReviewMapper.insert(songReviewDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMusic(MusicDeleteReqDTO requestParam) {
        // 创建审批任务
        SongDO songDO = SongDO.builder()
                .songId(requestParam.getSongId())
                .songStatus(SongStatusEnum.DELETED.getCode())
                .build();
        LambdaQueryWrapper<SongDO> queryWrapper = Wrappers.lambdaQuery(SongDO.class)
                .eq(SongDO::getSongId, requestParam.getSongId())
                .eq(SongDO::getDelFlag, 0)
                .eq(SongDO::getSongStatus, 1);
        SongDO oldSongDO = songMapper.selectOne(queryWrapper);
        if (oldSongDO == null) {
            throw new ServiceException("该歌曲不存在，无法删除");
        }
        LambdaUpdateWrapper<SongDO> updateWrapper = Wrappers.lambdaUpdate(SongDO.class)
                .eq(SongDO::getSongId, requestParam.getSongId())
                .set(SongDO::getSongStatus, 0);
        songMapper.update(updateWrapper);
        SongReviewDO songReviewDO = SongReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .originalData(JSON.toJSONString(oldSongDO))
                .nowData(JSON.toJSONString(songDO))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.DELETE.getCode())
                .build();
        songReviewMapper.insert(songReviewDO);
    }

    @Override
    @Transactional
    public void updateMusic(MusicUpdateReqDTO requestParam) {
        LambdaQueryWrapper<SongDO> queryWrapper = Wrappers.lambdaQuery(SongDO.class)
                .eq(SongDO::getSongId, requestParam.getSongId())
                .eq(SongDO::getDelFlag, 0)
                .eq(SongDO::getSongStatus, 1);
        SongDO oldSongDO = songMapper.selectOne(queryWrapper);
        if (oldSongDO == null) {
            throw new ServiceException("该歌曲不存在，怎么进行修改呢？");
        }
        LambdaUpdateWrapper<SongDO> updateWrapper = Wrappers.lambdaUpdate(SongDO.class)
                .eq(SongDO::getSongId, requestParam.getSongId())
                .set(SongDO::getSongStatus, 0);
        songMapper.update(updateWrapper);
        SongReviewDO songReviewDO = SongReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .originalData(JSON.toJSONString(oldSongDO))
                .nowData(JSON.toJSONString(BeanUtil.toBean(requestParam,SongDO.class)))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.UPDATE.getCode())
                .build();
        songReviewMapper.insert(songReviewDO);
    }

    @Override
    public List<SongQueryRespDTO> pageQuerySong(Integer page, Integer pageSize) {
        return songMapper.pageQuerySong(page - 1, pageSize);
    }

    @Override
    public List<SongQueryRespDTO> fuzzyInquiry(SongFuzzyInquiryReqDTO requestParam) {
        requestParam.setPage(requestParam.getPage() - 1);
        return songMapper.fuzzyInquiry(requestParam);
    }

}
