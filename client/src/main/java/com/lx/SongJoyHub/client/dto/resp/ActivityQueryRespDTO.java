package com.lx.SongJoyHub.client.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 活动查询返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityQueryRespDTO {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
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
