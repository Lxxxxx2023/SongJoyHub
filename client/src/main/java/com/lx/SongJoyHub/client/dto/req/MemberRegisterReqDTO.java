package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 会员注册请求参数实体
 */
@Data
public class MemberRegisterReqDTO {
    /**
     * 会员真实姓名
     */
    private String realName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 会员密码
     */
    private String password;

    /**
     * 生日
     */
    private Date birthday;
}
