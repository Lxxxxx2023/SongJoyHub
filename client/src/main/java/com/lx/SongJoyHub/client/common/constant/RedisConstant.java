package com.lx.SongJoyHub.client.common.constant;

/**
 * redis 常量
 */
public final class RedisConstant {
    /**
     * 歌曲信息缓存key
     */
    public static final String SONG_KEY = "song:%s";

    /**
     * 房间信息缓存key
     */
    public static final String ROOM_KEY = "room:%s";

    /**
     * 活动信息缓存key
     */
    public static final String ACTIVITY_KEY = "activity:%s";
    /**
     *
     */
    public static final String CAN_PARTAKE_KEY = "activity:can-partake:%s";
    /**
     *
     */
    public static final String ROOM_RESERVATION_KEY = "song:name:%s";
}
