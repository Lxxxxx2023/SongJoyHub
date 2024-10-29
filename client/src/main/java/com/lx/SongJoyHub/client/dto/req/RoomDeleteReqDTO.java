package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 删除房间请求参数
 */
@Data
public class RoomDeleteReqDTO {
    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 原因
     */
    private String cause;
}
