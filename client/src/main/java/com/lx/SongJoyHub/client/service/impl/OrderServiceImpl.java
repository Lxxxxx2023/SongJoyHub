package com.lx.SongJoyHub.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.dao.entity.MemberDO;
import com.lx.SongJoyHub.client.dao.entity.OrderDO;
import com.lx.SongJoyHub.client.dao.mapper.MemberMapper;
import com.lx.SongJoyHub.client.dao.mapper.OrderMapper;
import com.lx.SongJoyHub.client.dto.req.OrderPayReqDTO;
import com.lx.SongJoyHub.client.service.OrderService;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单业务逻辑实现层
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements OrderService {

    private final OrderMapper orderMapper;

    private final MemberMapper memberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(OrderPayReqDTO requestParam) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getId, requestParam.getOrderId())
                .eq(OrderDO::getOrderStatus,1)
                .eq(OrderDO::getUserId, UserContext.getUserId());
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if(orderDO == null) {
            throw new ServiceException("订单状态错误，可能是已取消或已完成");
        }
        int i = memberMapper.decrementBalance(orderDO.getPayableAmount(), Long.valueOf(UserContext.getUserId()));
        if(!SqlHelper.retBool(i)) {
            throw new ServiceException("用户余额不足无法支付");
        }
        LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                .eq(OrderDO::getId, orderDO.getId())
                .set(OrderDO::getOrderStatus,2);
        int update = orderMapper.update(updateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("订单状态修改失败");
        }
        // TODO 发放奖励
    }
}
