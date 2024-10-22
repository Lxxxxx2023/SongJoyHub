package com.lx.SongJoyHub.client.common.annotaion;

import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.context.UserInfoDTO;
import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;
import com.lx.SongJoyHub.framework.errorcode.BaseErrorCode;
import com.lx.SongJoyHub.framework.exception.ClientException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 权限检查切面类
 */
@Aspect
public final class AuthCheckAspect {
    @Around("@annotation(com.lx.SongJoyHub.client.common.annotaion.AuthCheck)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthCheck authCheck = getAuthCheckAnnotation(joinPoint);
        List<UserRoleEnum> roleList = Arrays.stream(authCheck.anyRole()).toList();
        // 无需验证
        for (UserRoleEnum role : roleList) {
            if(role == UserRoleEnum.NOLOGIN) {
                return joinPoint.proceed();
            }
        }

        UserInfoDTO user = UserContext.getUser();
        if(user == null) {
            throw new ClientException(BaseErrorCode.USER_LOGIN_ERROR);
        }

        UserRoleEnum userRole =UserContext.getUserRole();
        for (UserRoleEnum userRoleEnum : roleList) {
            if(userRole == userRoleEnum) {
                return joinPoint.proceed();
            }
        }
        // 未通过权限校验
        throw new ClientException(BaseErrorCode.USER_PERMISSION_FAIL);
    }

    public static AuthCheck getAuthCheckAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(AuthCheck.class);
    }
}