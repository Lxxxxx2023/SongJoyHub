package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 审核类型
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum ReviewTypeEnum {
    INSERT(1,"新增"),

    DELETE(0,"删除"),

    UPDATE(2,"更改");

    private Integer code;
    private String desc;
}
