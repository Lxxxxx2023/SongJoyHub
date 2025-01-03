package com.lx.SongJoyHub.client.common.context;

import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 用户角色
     */
    private UserRoleEnum userRole;
}
