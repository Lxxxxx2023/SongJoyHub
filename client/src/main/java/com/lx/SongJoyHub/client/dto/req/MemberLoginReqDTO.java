package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 *  会员登录参数实体类
 */
@Data
public class MemberLoginReqDTO {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 会员密码
     */
    private String password;
}
