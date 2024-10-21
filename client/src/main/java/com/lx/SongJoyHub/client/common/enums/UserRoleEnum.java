package com.lx.SongJoyHub.client.common.enums;


import com.lx.SongJoyHub.framework.errorcode.BaseErrorCode;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户角色枚举类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum UserRoleEnum {
    NOLOGIN(-1,"无需验证"),

    user(0,"普通用户"),

    admin(1,"管理员");

    private Integer code;
    private String role;

    /**
     * 通过 code 获取目标枚举对象
     *
     * @param code 枚举的 code
     * @return 对应的 UserRole 枚举对象
     */
    public static UserRoleEnum getByCode(Integer code) {
        for (UserRoleEnum userRole : UserRoleEnum.values()) {
            if (userRole.getCode().equals(code)) {
                return userRole;
            }
        }
        throw new ServiceException(BaseErrorCode.SERVICE_ERROR);
    }
}
