package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoomDO {

    @TableId(type = IdType.AUTO)
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
    private String roomType;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;
}