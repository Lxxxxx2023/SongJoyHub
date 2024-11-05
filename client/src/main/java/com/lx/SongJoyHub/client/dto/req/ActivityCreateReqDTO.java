package com.lx.SongJoyHub.client.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 创建活动请求参数
 */
@Data
public class ActivityCreateReqDTO {
    /**
     * 活动名
     */
    private String activityName;

    /**
     * 参加规则
     */
    private String receiveRule;

    /**
     * 奖励内容
     */
    private String rewardContent;
    /**
     * 活动类型
     */
    private Integer activityType;
    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validStartTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validEndTime;
}
