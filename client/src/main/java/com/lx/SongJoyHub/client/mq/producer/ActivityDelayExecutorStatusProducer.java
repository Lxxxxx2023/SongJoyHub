package com.lx.SongJoyHub.client.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.client.common.constant.RocketMQConstant;
import com.lx.SongJoyHub.client.mq.base.BaseSendExtendDTO;
import com.lx.SongJoyHub.client.mq.base.MessageWrapper;
import com.lx.SongJoyHub.client.mq.event.ActivityDelayEvent;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 活动定时关闭执行生产者
 */
@Component
public class ActivityDelayExecutorStatusProducer extends AbstractCommonSendProduceTemplate<ActivityDelayEvent>{

    public ActivityDelayExecutorStatusProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected BaseSendExtendDTO buildBaseSendExtendParam(ActivityDelayEvent messageSendEvent) {
        return BaseSendExtendDTO.builder()
                .keys(String.valueOf(messageSendEvent.getActivityId()))
                .topic(RocketMQConstant.ACTIVITY_DELAY_TOPIC_KEY)
                .delayTime(messageSendEvent.getDelayTime())
                .build();
    }

    @Override
    protected Message<?> buildMessage(ActivityDelayEvent messageSendEvent, BaseSendExtendDTO baseSendExtendDTO) {
        String keys = StrUtil.isEmpty(baseSendExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseSendExtendDTO.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(keys, messageSendEvent))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseSendExtendDTO.getTag())
                .build();
    }
}
