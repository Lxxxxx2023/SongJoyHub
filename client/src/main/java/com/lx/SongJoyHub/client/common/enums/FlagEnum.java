package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 标识枚举类
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FlagEnum {
    INCOMPLATE(0, "未完成 或 未删除"),

    complate(1, "已完成 或 已删除");

    private Integer code;

    private String flagName;

}
