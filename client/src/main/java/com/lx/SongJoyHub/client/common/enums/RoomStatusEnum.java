package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 房间状态枚举类
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RoomStatusEnum {
    VIABLE("可使用", 1),

    MAINTENANCE("在维修", 2),

    INREVIEW("在审核", 3),

    UNAVAILABLE("不可使用", 4),

    DELETED("已删除",5);
    /**
     * 状态
     */
    private String status;
    /**
     * 状态码
     */
    private Integer code;
}
