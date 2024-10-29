package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询房间请求参数
 */
@Data
public class RoomQueryReqDTO {
    /**
     * 房间名
     */
    private String roomName;

    /**
     * 最小价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;
    /**
     * 房间状态 0：未使用 1：正在使用 2：在维修 3：其他
     */
    private Integer status;

    /**
     * 房间类型
     */
    private Integer type;
}
