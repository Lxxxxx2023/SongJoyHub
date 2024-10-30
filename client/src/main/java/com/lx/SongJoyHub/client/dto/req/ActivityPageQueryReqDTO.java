package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 管理员分页查询活动入参
 */
@Data
public class ActivityPageQueryReqDTO {
    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNo;

    private Long activityId;

    /**
     * 活动名
     */
    private String activeName;

    /**
     * 活动状态 0 已开始 1 已结束
     */
    private int activityStatus;

    /**
     * 活动开始时间
     */
    private Date validStartTime;

    /**
     * 活动结束时间
     */
    private Date validEndTime;

}
