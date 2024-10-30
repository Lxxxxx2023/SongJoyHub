package com.lx.SongJoyHub.client.mq.event;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDelayEvent {
    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 延迟时间
     */
    private Long delayTime;

    /**
     * 状态
     */
    private Integer activityStatus;
}
