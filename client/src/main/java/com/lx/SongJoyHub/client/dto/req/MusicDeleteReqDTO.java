package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 音乐删除请求参数
 */
@Data
public class MusicDeleteReqDTO {
    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 原因
     */
    private String cause;
}
