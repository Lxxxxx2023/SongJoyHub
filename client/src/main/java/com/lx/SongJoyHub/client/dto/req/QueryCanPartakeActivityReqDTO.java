package com.lx.SongJoyHub.client.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询能参加活动请求参数
 */
@Data
public class QueryCanPartakeActivityReqDTO {
    /**
     * 小时数
     */
    private Integer timeCount;

    /**
     * 总价格
     */
    private BigDecimal amount;
}
