package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 多条件查询
 */
@Data
public class SongFuzzyInquiryReqDTO {
    /**
     * 歌曲名
     */
    private String songName;

    /**
     * 歌曲分类
     */
    private String category;

    /**
     * 歌曲语言
     */
    private String songLanguage;

    /**
     * 歌唱者
     */
    private String singer;

    /**
     * 歌曲状态
     */
    private Integer songStatus;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer page;
}
