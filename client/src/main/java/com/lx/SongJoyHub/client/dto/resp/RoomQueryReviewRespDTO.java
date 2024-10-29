package com.lx.SongJoyHub.client.dto.resp;

import lombok.Data;
import java.util.Date;

/**
 * 查询审核信息返回值实体类
 */
@Data
public class RoomQueryReviewRespDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 提交者id
     */
    private Long submitterId;
    /**
     * 提交者名称
     */
    private String submitterName;
    /**
     * 审核者id
     */
    private Long opId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 修改理由
     */
    private String cause;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 备注
     */
    private String notes;

    /**
     * 提交时间
     */
    private Date submitterTime;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 房间审核类型
     */
    private int type;
}
