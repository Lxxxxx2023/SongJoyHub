package com.lx.SongJoyHub.client.common.constant;

/**
 * RocketMQ常量类
 */
public  final class RocketMQConstant {
    /**
     * 活动延迟修改状态topic
     */
    public final static String ACTIVITY_DELAY_TOPIC_KEY = "song-joy-hub_activity_delay_topic_key";

    /**
     * 活动延迟修改状态消费者组
     */
    public final static String ACTIVITY_DELAY_GROUP_KEY = "song-joy-hub_activity_delay_group_key";

    /**
     * 订单超时取消状态topic
     */
    public final static String ORDER_TIMEOUT_TOPIC_KEY = "song-joy-hub_order_delay_topic_key";

    /**
     * 订单超时取消消费者组
     */
    public final static String ORDER_TIMEOUT_GROUP_KEY = "song-joy-hub_order_delay_group_key";
}
