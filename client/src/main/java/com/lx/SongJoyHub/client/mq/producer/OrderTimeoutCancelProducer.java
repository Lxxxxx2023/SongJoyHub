package com.lx.SongJoyHub.client.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.client.common.constant.RocketMQConstant;
import com.lx.SongJoyHub.client.mq.base.BaseSendExtendDTO;
import com.lx.SongJoyHub.client.mq.base.MessageWrapper;
import com.lx.SongJoyHub.client.mq.event.OrderTimeoutCancelEvent;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderTimeoutCancelProducer extends AbstractCommonSendProduceTemplate<OrderTimeoutCancelEvent>{


    public OrderTimeoutCancelProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected BaseSendExtendDTO buildBaseSendExtendParam(OrderTimeoutCancelEvent messageSendEvent) {
        return BaseSendExtendDTO.builder()
                .topic(RocketMQConstant.ORDER_TIMEOUT_TOPIC_KEY)
                .eventName("订单超时取消@变更订单状态和预约状态")
                .keys(messageSendEvent.getOrderId().toString())
                .delayTime(messageSendEvent.getDelayTime())
                .build();
    }

    @Override
    protected Message<?> buildMessage(OrderTimeoutCancelEvent messageSendEvent, BaseSendExtendDTO baseSendExtendDTO) {
        String keys = StrUtil.isEmpty(baseSendExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseSendExtendDTO.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(keys, messageSendEvent))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseSendExtendDTO.getTag())
                .build();
    }
}
