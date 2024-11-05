package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 多条件查询房间审核信息请求参数
 */
@Data
public class RoomReviewMultipleQueryReqDTO {
    private Long id;

    private Integer status;

    private Long committerId;

    private Long opId;

    private Integer type;

    private Date minCreateTime;

    private Date maxCreateTime;

    private Date minUpdateTime;

    private Date maxUpdateTime;

    private Integer page;

    private Integer pageSize;
}
