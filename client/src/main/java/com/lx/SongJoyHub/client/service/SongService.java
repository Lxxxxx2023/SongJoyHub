package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;


/**
 * 歌曲业务逻辑层
 */
public interface SongService extends IService<SongDO> {
    /**
     * 添加歌曲
     * @param requestParam 添加歌曲请求入参
     */
    void addMusic(MusicCreateReqDTO requestParam);
}
