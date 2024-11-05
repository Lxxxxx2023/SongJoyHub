package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 添加音乐请求参数
 */
@Data
public class MusicCreateReqDTO {
    /**
     * 歌曲名
     */
    private String songName;

    /**
     * 演唱者
     */
    private String singer;

    /**
     * 歌曲语种
     */
    private String songLanguage;

    /**
     * 歌曲简介
     */
    private String introduction;

    /**
     * 歌曲存放位置
     */
    private String songAddress;

    /**
     * 分类
     */
    private String category;

    /**
     * 歌曲时长
     */
    private String duration;
    /**
     * 歌词存放位置
     */
    private String lyric;

    /**
     * 原因
     */
    private String cause;
}
