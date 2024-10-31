package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_activity")
public class ActivityDO {

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
    * 活动状态 1 已开始 0 已结束
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

    /**
     * 活动类型
     */
    private Integer activityType;
    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 删除标志
    */
    private int delFlag;

}