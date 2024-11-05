package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicDeleteReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicUpdateReqDTO;
import com.lx.SongJoyHub.client.dto.req.SongFuzzyInquiryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongQueryRespDTO;
import com.lx.SongJoyHub.framework.result.Result;

import java.util.List;


/**
 * 歌曲业务逻辑层
 */
public interface SongService extends IService<SongDO> {
    /**
     * 添加歌曲
     * @param requestParam 添加歌曲请求入参
     */
    void addMusic(MusicCreateReqDTO requestParam);

    /**
     * 修改音乐信息
     * @param requestParam 删除音乐请求参数
     */
    void deleteMusic(MusicDeleteReqDTO requestParam);

    /**
     * 更新音乐信息
     * @param requestParam 更新音乐请求参数
     */
    void updateMusic(MusicUpdateReqDTO requestParam);

    /**
     * 分页查询歌曲信息
     * @param page 页码
     * @param pageSize 页大小
     * @return 歌曲信息
     */
    List<SongQueryRespDTO> pageQuerySong(Integer page, Integer pageSize);

    /**
     * 多条件分页查询
     * @param requestParam 多条件分页查询请求参数
     * @return 多条件分页查询结果
     */
    List<SongQueryRespDTO> fuzzyInquiry(SongFuzzyInquiryReqDTO requestParam);
}
