package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 店长添加管理员账号
 */
@Data
public class AdminAddReqDTO {
    /**
     * 管理员真实名
     */
    private String name;

    /**
     * 管理员手机号码
     */
    private String phone;

    /**
     * 管理员权限等级
     */
    private Integer level;
}
