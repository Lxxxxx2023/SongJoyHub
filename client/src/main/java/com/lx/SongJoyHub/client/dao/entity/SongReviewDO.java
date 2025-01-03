package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_song_review")
public class SongReviewDO  {

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
     * 修改前歌曲信息
     */
    private String originalData;

    /**
     * 歌曲具体信息
     */
    private String nowData;

    /**
     * 备注
     */
    private String notes;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 类型
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}