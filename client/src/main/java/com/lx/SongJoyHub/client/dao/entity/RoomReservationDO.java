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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_room_reservation")
public class RoomReservationDO  {


    @TableId(type = IdType.AUTO)
    private Long reservationId;
    /**
    * 房间id
    */
    private Long roomId;

    /**
     * 用户id
     */
    private Long userId;
    /**
    * 预约开始时间
    */
    private Date startTime;

    /**
    * 预约结束时间
    */
    private Date endTime;


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
    private Integer status;
}