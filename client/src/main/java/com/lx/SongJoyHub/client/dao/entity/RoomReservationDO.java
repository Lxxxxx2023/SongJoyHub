package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoomReservationDO  {


    @TableId(type = IdType.AUTO)
    private Long reservationId;
    /**
    * 房间id
    */
    private Long roomId;

    /**
    * 预约开始时间
    */
    private Date startTime;

    /**
    * 预约结束时间
    */
    private Date endTime;

    /**
    * 支付的金额
    */
    private BigDecimal payAmount;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 是否有效
    */
    private int status;
}