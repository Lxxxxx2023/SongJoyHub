package com.lx.SongJoyHub.client.dto.req;


import lombok.Data;

/**
 * 管理员审核请求参数
 */
@Data
public class RoomReviewReqDTO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 备注
     */
    private String notes;
}
