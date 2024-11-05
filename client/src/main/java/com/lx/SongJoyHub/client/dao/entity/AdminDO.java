package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_admin")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDO  {

    /**
     * 管理员id
     */
    @TableId(type = IdType.AUTO)
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
    * 密码
    */
    private String password;

    /**
    * 注册时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
     *  权限等级
     */
    private Integer level;
}