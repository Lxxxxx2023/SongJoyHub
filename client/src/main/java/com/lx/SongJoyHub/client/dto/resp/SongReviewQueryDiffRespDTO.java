package com.lx.SongJoyHub.client.dto.resp;

import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 歌曲审核分页查询查询返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongReviewQueryDiffRespDTO {
    /**
     * 旧数据
     */
    private SongDO oldData;
    /**
     * 新数据
     */
    private SongDO newData;
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
