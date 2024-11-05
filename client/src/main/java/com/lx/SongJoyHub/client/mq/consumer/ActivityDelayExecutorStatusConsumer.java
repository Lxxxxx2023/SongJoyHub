package com.lx.SongJoyHub.client.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.lx.SongJoyHub.client.common.constant.RocketMQConstant;
import com.lx.SongJoyHub.client.dao.entity.ActivityDO;
import com.lx.SongJoyHub.client.dao.mapper.ActivityMapper;
import com.lx.SongJoyHub.client.mq.base.MessageWrapper;
import com.lx.SongJoyHub.client.mq.event.ActivityDelayEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 活动延迟修改状态消费者
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RocketMQConstant.ACTIVITY_DELAY_TOPIC_KEY,
        consumerGroup = RocketMQConstant.ACTIVITY_DELAY_GROUP_KEY
)
public class ActivityDelayExecutorStatusConsumer implements RocketMQListener<MessageWrapper<ActivityDelayEvent>> {

    private final ActivityMapper activityMapper;

    @Override
    public void onMessage(MessageWrapper<ActivityDelayEvent> messageWrapper) {
        log.info("[消费者] 活动定期结束变更记录发送状态 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));
        ActivityDelayEvent message = messageWrapper.getMessage();
        ActivityDO activityDO = ActivityDO.builder()
                .activityId(message.getActivityId())
                .activityStatus(message.getActivityStatus()).build();
        activityMapper.updateById(activityDO);
    }
}
