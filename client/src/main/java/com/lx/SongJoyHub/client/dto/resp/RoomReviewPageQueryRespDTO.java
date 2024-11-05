package com.lx.SongJoyHub.client.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 房间预约分页查询
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomReviewPageQueryRespDTO {

    private Long id;
    /**
     * 提交者id
     */
    private Long committerId;

    /**
     * 提交者名
     */
    private String committerName;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 审核情况
     */
    private Integer status;
}
