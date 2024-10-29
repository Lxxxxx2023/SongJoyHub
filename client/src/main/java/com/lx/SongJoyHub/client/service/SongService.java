package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicDeleteReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicUpdateReqDTO;


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
}
