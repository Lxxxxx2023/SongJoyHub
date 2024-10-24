package com.lx.SongJoyHub.music.dao.entity;

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
public class SongReviewDO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 提交者id
    */
    private Long submitterId;

    /**
    * 审批者id
    */
    private Long opId;

    /**
    * 歌曲id
    */
    private Long songId;

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
    private Date submitTime;

    /**
    * 审批时间
    */
    private Date examineTime;
}