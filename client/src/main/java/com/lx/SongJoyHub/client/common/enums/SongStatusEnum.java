package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 歌曲状态枚举类
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SongStatusEnum {
    DELETED(2, "已删除"),
    PLAYABLE(1, "可播放"),

    INREVIEW(0, "在审核");
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态
     */
    private String status;
}
