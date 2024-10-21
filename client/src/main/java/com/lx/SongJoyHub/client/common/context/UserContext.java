package com.lx.SongJoyHub.client.common.context;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;

import java.util.Optional;

/**
 * 用户上下文
 */
public final class UserContext {

    private static final TransmittableThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置用户至上下文
     *
     * @param user 用户详情信息
     */
    public static void setUser(UserInfoDTO user) {
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public static String getUserId() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    /**
     * 获取用户上下文
     */
    public static UserInfoDTO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获取用户角色
     */
    public static UserRoleEnum getUserRole() {
        return USER_THREAD_LOCAL.get().getUserRole();
    }
    /**
     * 清理用户上下文
     */
    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}
