package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomReviewDO {


    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 提交者id
     */
    private Long committerId;

    /**
     * 提交者名
     */
    private String committerName;

    /**
     * 处理者id
     */
    private Long opId;

    /**
     * 处理者名
     */
    private String opName;

    /**
     * 原因
     */
    private String cause;

    /**
     * 修改前房间信息
     */
    private String originalData;

    /**
     * 歌曲房间信息
     */
    private String nowData;

    /**
     * 备注
     */
    private String notes;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 事件类型
     */
    private int type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}