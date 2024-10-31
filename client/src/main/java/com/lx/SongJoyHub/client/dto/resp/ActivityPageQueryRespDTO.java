package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPageQueryRespDTO {
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 活动名
     */
    private String activeName;

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
     * 活动状态 0 已开始 1 已结束
     */
    private int status;

    /**
     * 活动开始时间
     */
    private Date validStartTime;

    /**
     * 活动结束时间
     */
    private Date validEndTime;

}
