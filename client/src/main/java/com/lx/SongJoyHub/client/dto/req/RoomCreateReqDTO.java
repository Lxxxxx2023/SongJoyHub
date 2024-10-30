package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建房间请求参数
 */
@Data
public class RoomCreateReqDTO {
    /**
     * 房间名
     */
    private String roomName;

    /**
     * 房间价格 以小时为单位
     */
    private BigDecimal price;

    /**
     * 房间介绍
     */
    private String introduction;

    /**
     * 房间类型
     */
    private Integer roomType;
}
