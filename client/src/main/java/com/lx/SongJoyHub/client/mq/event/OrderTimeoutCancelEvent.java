package com.lx.SongJoyHub.client.mq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单超时取消
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTimeoutCancelEvent {
    /**
     * 预约id
     */
    private Long reservationId;
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 取消时间
     */
    private Long delayTime;
}
