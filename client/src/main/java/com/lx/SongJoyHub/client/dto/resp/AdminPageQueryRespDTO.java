package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询分页所有员工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPageQueryRespDTO {

    private Long id;
    /**
     * 员工名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String icon;

    /**
     * 昵称
     */
    private String nick;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     *  权限等级
     */
    private Integer level;
}
