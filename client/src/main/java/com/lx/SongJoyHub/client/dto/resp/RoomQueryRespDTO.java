package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomQueryRespDTO {
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
    private Integer roomStatus;

    /**
     * 房间介绍
     */
    private String introduction;

    /**
     * 房间类型
     */
    private Integer type;
}
