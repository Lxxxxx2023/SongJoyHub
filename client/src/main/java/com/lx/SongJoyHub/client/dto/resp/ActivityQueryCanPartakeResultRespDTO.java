package com.lx.SongJoyHub.client.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityQueryCanPartakeResultRespDTO {
    /**
     * 能参与的减免活动列表
     */
    private List<ActivityQueryCanPartakeRespDTO> reliefLList;

    /**
     * 能参与的方法火活动的列表
     */
    private List<ActivityQueryCanPartakeRespDTO> grantList;
    /**
     * 活动总优惠
     */
    private BigDecimal discount_amount;

}
