package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 房间滚动分页查询
 */
@Data
public class RoomRollQueryReqDTO {

    private Long maxId;

    private Integer pageSize;
}
