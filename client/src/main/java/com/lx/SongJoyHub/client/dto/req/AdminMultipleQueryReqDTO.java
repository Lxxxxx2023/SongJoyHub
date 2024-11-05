package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

/**
 * 管理员多条件查询请求参数
 */
@Data
public class AdminMultipleQueryReqDTO {
    /**
     * 管理员名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * id
     */
    private Long id;

    /**
     * 权限等级
     */
    private Integer level;

    /**
     * 页码
     */
    private Integer page;
    /**
     * 页大小
     */
    private Integer pageSize;
}
