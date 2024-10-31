package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.OrderDO;
import com.lx.SongJoyHub.client.dto.req.OrderPayReqDTO;

/**
 * 订单业务逻辑层
 */
public interface OrderService extends IService<OrderDO> {

    /**
     * 用户支付订单
     * @param requestParam 用户支付订单请求参数
     */
    void payOrder(OrderPayReqDTO requestParam);
}
