package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 歌曲实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_song")
public class SongDO  {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long songId;
    /**
    * 歌曲名
    */
    private String songName;

    /**
    * 演唱者
    */
    private String singer;

    /**
    * 歌曲语种
    */
    private String songLanguage;

    /**
    * 歌曲简介
    */
    private String introduction;

    /**
    * 歌曲存放位置
    */
    private String songAddress;

    /**
    * 分类
    */
    private String category;

    /**
    * 歌曲时长
    */
    private String duration;

    /**
    * 歌词存放位置
    */
    private String lyric;

    /**
    * 注册时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 歌曲状态 0：在审核 1 可播放
    */
    private Integer songStatus;

    /**
    * 删除标志
    */
    private Integer delFlag;
}