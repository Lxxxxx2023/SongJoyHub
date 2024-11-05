package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询用户能参与的活动
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityQueryCanPartakeRespDTO {
    /**
     * 主键id
     */
    private Long activityId;

    /**
     * 活动名
     */
    private String activityName;

    /**
     * 奖励内容
     */
    private String rewardContent;

}
