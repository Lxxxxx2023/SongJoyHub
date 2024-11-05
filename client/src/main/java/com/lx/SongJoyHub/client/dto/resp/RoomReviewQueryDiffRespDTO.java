package com.lx.SongJoyHub.client.dto.resp;

import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询审核情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomReviewQueryDiffRespDTO {
    /**
     * 旧数据
     */
    private RoomDO oldData;
    /**
     * 新数据
     */
    private RoomDO newData;
    /**
     * 提交人
     */
    private String committerName;
    /**
     * 审核人
     */
    private String opName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 审核时间
     */
    private Date updateTime;
    /**
     * 原因
     */
    private String cause;
    /**
     * 意见
     */
    private String notes;
    /**
     * 事件类型
     */
    private Integer type;
}
