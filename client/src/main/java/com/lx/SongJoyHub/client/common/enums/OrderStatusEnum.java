package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 订单状态枚举类
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OrderStatusEnum {

    cancel(3, "已取消"),

    pay(2, "已支付"),

    unpaid(1, "未支付");

    private Integer code;

    private String status;
}
