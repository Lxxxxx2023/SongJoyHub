package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 房间上限请求参数
 */
@Data
public class RoomOnLineReqDTO {

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 上线原因
     */
    private String cause;
}
