package com.lx.SongJoyHub.client.common.annotaion;

import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 * 默认无需任何权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    UserRoleEnum[] anyRole() default UserRoleEnum.NOLOGIN;
}
