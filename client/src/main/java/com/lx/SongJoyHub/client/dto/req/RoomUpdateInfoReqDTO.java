package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 修改房间信息请求参数
 */
@Data
public class RoomUpdateInfoReqDTO {
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间价格 以小时为单位
     */
    private BigDecimal price;


    /**
     * 房间介绍
     */
    private String desc;
    /**
     * 房间类型
     */
    private Integer type;
}
