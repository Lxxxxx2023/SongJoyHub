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
     * 审批者id
     */
    private Long opId;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 备注
     */
    private String notes;

    /**
     * 审批时间
     */
    private Date examineTime;
}
