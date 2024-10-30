package com.lx.SongJoyHub.client.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参加活动请求参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPartakeReqDTO {

    /**
     * 活动主键
     */
    private Long activityId;
}
