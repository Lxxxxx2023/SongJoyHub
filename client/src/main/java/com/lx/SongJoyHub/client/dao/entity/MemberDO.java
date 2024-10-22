package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  会员实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_member")
public class MemberDO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 会员真实姓名
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
    * 用户昵称
    */
    private String nick;

    /**
    * 会员密码
    */
    private String password;

    /**
    * 生日
    */
    private Date birthday;

    /**
    * 账号余额
    */
    private BigDecimal balance;

    /**
    * 用户积分
    */
    private Integer points;

    /**
    * 初始等级
    */
    private Long levelId;

    /**
    * 注册时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

}