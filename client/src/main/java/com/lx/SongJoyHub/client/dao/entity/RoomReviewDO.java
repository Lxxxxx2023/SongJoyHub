package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomReviewDO {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 提交者id
    */
    private Long submitterId;
    /**
     * 提交者名称
     */
    private String submitterName;
    /**
    * 审核者id
    */
    private Long opId;

    /**
    * 房间id
    */
    private Long roomId;

    /**
    * 修改理由
    */
    private String cause;

    /**
    * 审核状态
    */
    private int status;

    /**
    * 备注
    */
    private String notes;

    /**
    * 提交时间
    */
    private Date submitterTime;

    /**
    * 审核时间
    */
    private Date examineTime;

    /**
    * 房间审核类型
    */
    private int type;
}