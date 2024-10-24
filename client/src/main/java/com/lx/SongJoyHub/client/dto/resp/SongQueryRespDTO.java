package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌曲查询返回实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongQueryRespDTO {
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
}
