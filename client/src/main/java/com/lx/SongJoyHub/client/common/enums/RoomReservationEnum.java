package com.lx.SongJoyHub.client.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 房间预约状态
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RoomReservationEnum {
    CANCEL(0, "已取消"),

    VALID(1, "预约有效"),

    INEFFECT(2, "生效中"),

    USED(3, "已使用");

    private Integer code;

    private String status;
}
