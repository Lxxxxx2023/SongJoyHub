package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 *  房间下线请求参数
 */
@Data
public class RoomOffLineReqDTO {
    /**
     * 下线房间id
     */
    private Long roomId;
    /**
     * 下线原因
     */
    private String cause;

}
