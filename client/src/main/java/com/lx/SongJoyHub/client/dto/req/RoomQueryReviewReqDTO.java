package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 查询房间审核信息请求参数
 */
@Data
public class RoomQueryReviewReqDTO {
    /**
     * 提交者id
     */
    private Long submitterId;

    /**
     * 审核者id
     */
    private Long opId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 最早时间
     */
    private Date minSubmitterTime;
    /**
     * 最晚时间
     */
    private Date maxSubmitterTime;

    /**
     * 最小审核时间
     */
    private Date minExamineTime;
    /**
     * 最大审核时间
     */
    private Date maxExamineTime;

    /**
     * 房间审核类型
     */
    private int type;
}
