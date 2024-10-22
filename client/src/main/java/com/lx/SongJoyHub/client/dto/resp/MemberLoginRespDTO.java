package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员登录返回实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRespDTO {
    /**
     * 登录token
     */
    private String accessToken;
}
