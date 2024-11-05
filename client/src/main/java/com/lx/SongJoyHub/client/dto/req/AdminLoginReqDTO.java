package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 管理员登录请求参数
 */
@Data
public class AdminLoginReqDTO {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 会员密码
     */
    private String password;
}
