package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 歌曲信息更新请求参数
 */
@Data
public class MusicUpdateReqDTO {
    /**
     * 主键
     */
    private Long songId;
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
    private String desc;

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
     * 修改原因
     */
    private String cause;
}
