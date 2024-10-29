package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 查询所有房间请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomQueryAllRespDTO {
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间名
     */
    private String roomName;

    /**
     * 房间价格 以小时为单位
     */
    private BigDecimal price;

    /**
     * 房间状态 0：未使用 1：正在使用 2：在维修 3：其他
     */
    private Integer status;

    /**
     * 房间介绍
     */
    private String desc;

    /**
     * 房间类型
     */
    private Integer type;
}
