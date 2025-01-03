package com.lx.SongJoyHub.client.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_room")
public class RoomDO {

    @TableId(type = IdType.AUTO)
    /**
     * 房间id
     */
    private Long roomId;
    /**
    * 房间名
    */
    private String roomName;

    /**
    * 房间价格 以小时为单位
    */
    private BigDecimal price;

    /**
    * 房间状态 0：未使用 1：正在使用 2：在维修 3：其他
    */
    private Integer roomStatus;

    /**
    * 房间介绍
    */
    private String introduction;

    /**
     * 房间类型
     */
    private Integer roomType;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;
}