package com.lx.SongJoyHub.client.mq.consumer;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.constant.RocketMQConstant;
import com.lx.SongJoyHub.client.common.enums.OrderStatusEnum;
import com.lx.SongJoyHub.client.common.enums.RoomReservationEnum;
import com.lx.SongJoyHub.client.dao.entity.OrderDO;
import com.lx.SongJoyHub.client.dao.entity.RoomReservationDO;
import com.lx.SongJoyHub.client.dao.mapper.OrderMapper;
import com.lx.SongJoyHub.client.dao.mapper.RoomReservationMapper;
import com.lx.SongJoyHub.client.mq.base.MessageWrapper;
import com.lx.SongJoyHub.client.mq.event.OrderTimeoutCancelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 订单超时取消
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RocketMQConstant.ORDER_TIMEOUT_TOPIC_KEY,
        consumerGroup = RocketMQConstant.ORDER_TIMEOUT_GROUP_KEY
)
public class OrderTimeoutCancelConsumer implements RocketMQListener<MessageWrapper<OrderTimeoutCancelEvent>> {

    private final OrderMapper orderMapper;

    private final RoomReservationMapper roomReservationMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MessageWrapper<OrderTimeoutCancelEvent> messageWrapper) {
        log.info("[消费者] 订单超时取消@变更订单状态和预约状态 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));
        OrderTimeoutCancelEvent orderTimeoutCancelEvent = messageWrapper.getMessage();
        OrderDO orderDO = orderMapper.selectById(orderTimeoutCancelEvent.getOrderId());
        if (orderDO.getOrderStatus() == OrderStatusEnum.cancel.getCode() || orderDO.getOrderStatus() == OrderStatusEnum.pay.getCode()) {
            return;
        }
        DateTime dateTime = new DateTime();
        LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                .eq(OrderDO::getId, orderTimeoutCancelEvent.getOrderId())
                .set(OrderDO::getOrderStatus, OrderStatusEnum.cancel.getCode())
                .set(OrderDO::getUpdateTime, dateTime);
        orderMapper.update(updateWrapper);
        RoomReservationDO roomReservationDO = roomReservationMapper.selectById(orderTimeoutCancelEvent.getReservationId());
        LambdaUpdateWrapper<RoomReservationDO> updateRoomReservation = Wrappers.lambdaUpdate(RoomReservationDO.class)
                .eq(RoomReservationDO::getReservationId, orderTimeoutCancelEvent.getReservationId())
                .set(RoomReservationDO::getStatus, RoomReservationEnum.CANCEL.getCode())
                .set(RoomReservationDO::getUpdateTime, dateTime);
        roomReservationMapper.update(updateRoomReservation);
        DateTime startTime = DateTime.of(roomReservationDO.getStartTime());
        DateTime endTime = DateTime.of(roomReservationDO.getEndTime());
        stringRedisTemplate.opsForZSet().removeRangeByScore(String.format(RedisConstant.RECOMMEND_KEY, roomReservationDO.getRoomId()),
                startTime.getTime(),
                endTime.getTime());
    }
}
