package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 房间预约请求参数
 */
@Data
public class RoomReservationReqDTO {
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
}
