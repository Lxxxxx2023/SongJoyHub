package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 歌曲审批入参
 */
@Data
public class SongReviewReqDTO {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 备注
     */
    private String notes;

    /**
     * 歌曲具体信息
     */
    private String nowData;

    /**
     * 审批状态
     */
    private Integer status;
}
