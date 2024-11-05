package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询订单返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderQueryRespDTO {

    private Long orderId;

    private String roomName;

    private Integer roomType;

    private BigDecimal totalAmount;

    private BigDecimal payableAmount;

    private BigDecimal discountAmount;

    private Integer orderStatus;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;
}
