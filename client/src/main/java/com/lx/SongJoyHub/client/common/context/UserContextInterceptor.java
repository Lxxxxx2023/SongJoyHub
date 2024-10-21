package com.lx.SongJoyHub.client.common.context;

import com.lx.SongJoyHub.client.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Nullable;

/**
 * 用户上下文拦截器
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        UserInfoDTO userInfo;
        try {
            userInfo = JWTUtil.parseJwtToken(token);
            UserContext.setUser(userInfo);
        } catch (Exception ignored) {
        }
        return true;
    }


    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
