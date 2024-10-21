//package com.lx.SongJoyHub.framework.annotaion;
//
//
//import com.lx.SongJoyHub.framework.errorcode.BaseErrorCode;
//import com.lx.SongJoyHub.framework.exception.ClientException;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * 用户访问权限控制类
// */
//@Aspect
//public final class AuthCheckAspect {
//    @Around("@annotation(com.lx.songjoyhub.joyservice.common.annotaion.AuthCheck)")
//    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
//        AuthCheck authCheck = getAuthCheckAnnotation(joinPoint);
//        List<UserRoleEnum> roleList = Arrays.stream(authCheck.anyRole()).toList();
//        // 无需验证
//        for (UserRoleEnum role : roleList) {
//            if(role == UserRoleEnum.NOLOGIN) {
//                return joinPoint.proceed();
//            }
//        }
//
//        // 已登录
//        UserInfoDTO user = UserContext.getUser();
//        if(user == null) {
//            throw new ClientException(BaseErrorCode.USER_LOGIN_ERROR);
//        }
//        UserRoleEnum userRole =UserContext.getUserRole();
//        for (UserRoleEnum userRoleEnum : roleList) {
//            if(userRole == userRoleEnum) {
//                return joinPoint.proceed();
//            }
//        }
//        // 未通过权限校验
//        throw new ClientException(BaseErrorCode.USER_PERMISSION_FAIL);
//    }
//
//    public static AuthCheck getAuthCheckAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
//        return targetMethod.getAnnotation(AuthCheck.class);
//    }
//}
