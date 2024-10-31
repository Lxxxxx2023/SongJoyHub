package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;

@Data
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
}