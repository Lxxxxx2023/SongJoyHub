package com.lx.SongJoyHub.client.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.SongStatusEnum;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.entity.UpdateSongReviewBO;
import com.lx.SongJoyHub.client.dao.mapper.SongMapper;
import com.lx.SongJoyHub.client.dao.mapper.SongReviewMapper;
import com.lx.SongJoyHub.client.dto.req.SongMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewQueryDiffRespDTO;
import com.lx.SongJoyHub.client.service.SongReviewService;
import com.lx.SongJoyHub.client.util.RedisUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


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
    public List<SongReviewPageQueryRespDTO> pageQuerySongReview(Integer page, Integer pageSize) {
        List<SongReviewPageQueryRespDTO> songReviewPageQueryRespDTOS = songReviewMapper.pageQuerySongReview(page, pageSize);
        System.out.println(songReviewPageQueryRespDTOS);
        return songReviewPageQueryRespDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineSaveMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        UpdateSongReviewBO updateSongReviewBO = buildUpdateSongReview(requestParam);
        int update = songReviewMapper.updateSongReview(updateSongReviewBO);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批失败 id: " + requestParam.getId());
        }
        if(requestParam.getStatus() != 1) return;
        // 审核通过 歌曲入库
        SongReviewDO songReviewDO = songReviewMapper.selectById(requestParam.getId());
        SongDO songDO = JSON.parseObject(songReviewDO.getNowData(), SongDO.class);
        songDO.setSongStatus(SongStatusEnum.PLAYABLE.getCode());
        try{
            songMapper.insert(songDO);
        }catch (DuplicateKeyException e) {
            throw new ServiceException("该歌手的该歌曲已存在");
        }
        // 创建缓存
        refactorSongCache(songDO);
    }

    private UpdateSongReviewBO buildUpdateSongReview(SongReviewReqDTO requestParam) {
        return UpdateSongReviewBO.builder()
                .notes(requestParam.getNotes())
                .songReviewId(requestParam.getId())
                .level(UserContext.getUser().getLevel())
                .opId(Long.valueOf(UserContext.getUserId()))
                .opName(UserContext.getUser().getUserName())
                .status(requestParam.getStatus())
                .build();

    }


    @Override
    public void examineDeleteMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        UpdateSongReviewBO updateSongReviewBO = buildUpdateSongReview(requestParam);
        int update = songReviewMapper.updateSongReview(updateSongReviewBO);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批删除歌曲失败 id: " + requestParam.getId());
        }

        SongReviewDO songReviewDO = songReviewMapper.selectById(requestParam.getId());
        SongDO songDO = JSON.parseObject(songReviewDO.getNowData(), SongDO.class);
        if(requestParam.getStatus() != 1) {
            songMapper.restoreSongStatus(songDO.getSongId());
            return;
        }
        LambdaUpdateWrapper<SongDO> updateWrapperSong = Wrappers.lambdaUpdate(SongDO.class)
                .eq(SongDO::getSongId, songDO.getSongId())
                .set(SongDO::getSongStatus,SongStatusEnum.DELETED.getCode())
                .set(SongDO::getDelFlag,1); // 标记为删除
        songMapper.update(updateWrapperSong);
        stringRedisTemplate.delete(String.format(RedisConstant.SONG_KEY, songDO.getSongId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examineUpdateMusic(SongReviewReqDTO requestParam) {
        // 处理审核信息
        UpdateSongReviewBO updateSongReviewBO = buildUpdateSongReview(requestParam);
        int update = songReviewMapper.updateSongReview(updateSongReviewBO);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("审批修改歌曲失败 id: " + requestParam.getId());
        }
        SongReviewDO songReviewDO = songReviewMapper.selectById(requestParam.getId());
        SongDO songDO = JSON.parseObject(songReviewDO.getNowData(), SongDO.class);
        if(requestParam.getStatus() != 1) {
            songMapper.restoreSongStatus(songDO.getSongId());
            return;
        }
        int updateSong = songMapper.updateSong(songDO);
        if(!SqlHelper.retBool(updateSong)) {
            throw new ServiceException("审核歌曲修改 id: " + requestParam.getId());
        }
        SongDO newSong = songMapper.selectById(songDO.getSongId());
        // 重构缓存
        refactorSongCache(newSong);
    }

    @Override
    public List<SongReviewPageQueryRespDTO> multipleQuerySongReview(SongMultipleQueryReqDTO requestParam) {
        requestParam.setPage(requestParam.getPage() - 1);
        return songReviewMapper.multipleQuerySongReview(requestParam);
    }

    @Override
    public SongReviewQueryDiffRespDTO querySongReviewDiff(Long id) {
        SongReviewDO songReviewDO = songReviewMapper.selectById(id);
        SongDO nowData = JSON.parseObject(songReviewDO.getNowData(), SongDO.class);
        SongDO oldData = JSON.parseObject(songReviewDO.getOriginalData(), SongDO.class);
        return SongReviewQueryDiffRespDTO.builder()
                .cause(songReviewDO.getCause())
                .oldData(oldData)
                .newData(nowData)
                .committerName(songReviewDO.getCommitterName())
                .opName(songReviewDO.getOpName())
                .createTime(songReviewDO.getCreateTime())
                .updateTime(songReviewDO.getUpdateTime())
                .notes(songReviewDO.getNotes())
                .type(songReviewDO.getType())
                .build();
    }

    private void refactorSongCache(SongDO songDO) {
        //存入缓存中
        SongQueryRespDTO songQueryRespDTO = BeanUtil.toBean(songDO, SongQueryRespDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(songQueryRespDTO, false, true);
        RedisUtil.convertHash(String.format(RedisConstant.SONG_KEY,songDO.getSongId())
                ,stringRedisTemplate
                ,cacheTargetMap
                ,String.valueOf(Instant.now().getEpochSecond() + 100000)
        );
    }
}
