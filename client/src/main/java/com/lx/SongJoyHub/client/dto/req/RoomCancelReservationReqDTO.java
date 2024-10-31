package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 取消预约房间参数
 */
@Data
public class RoomCancelReservationReqDTO {
    /**
     * 订单id
     */
    private Long orderId;
}
