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
     * 备注
     */
    private String notes;


    /**
     * 审批状态
     */
    private Integer status;
}
