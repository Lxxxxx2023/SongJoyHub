package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查看审核情况返回值
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongReviewRespDTO {
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
     * 处理者id
     */
    private Long opId;

    /**
     * 处理者名
     */
    private String opName;
    /**
     * 原因
     */
    private String cause;

    /**
     * 修改前歌曲信息
     */
    private String originalData;

    /**
     * 歌曲具体信息
     */
    private String nowData;

    /**
     * 备注
     */
    private String notes;

    /**
     * 类型
     */
    private Integer type;
}
