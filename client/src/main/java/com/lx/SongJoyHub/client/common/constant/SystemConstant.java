package com.lx.SongJoyHub.client.common.constant;

import lombok.Data;

/**
 * 系统常量
 */
@Data
public final class SystemConstant {

    /**
     * 用户密码正则表达式
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,18}$";

    /**
     * 用户手机号码正则表达式
     */
    public static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";
}
