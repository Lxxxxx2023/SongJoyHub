package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 用户支付订单请求参数
 */
@Data
public class OrderPayReqDTO {
    /**
     * 订单id
     */
    private Long orderId;
}
