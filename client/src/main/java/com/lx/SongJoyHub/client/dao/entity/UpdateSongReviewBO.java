package com.lx.SongJoyHub.client.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新歌曲审核信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSongReviewBO {
    /**
     * 歌曲审核id
     */
    private Long songReviewId;

    /**
     * 意见
     */
    private String notes;

    /**
     * 操作者id
     */
    private Long opId;

    /**
     * 操作者名
     */
    private String opName;

    /**
     * 是否同意
     */
    private Integer status;
    /**
     * 用户等级
     */
    private Integer level;
}
