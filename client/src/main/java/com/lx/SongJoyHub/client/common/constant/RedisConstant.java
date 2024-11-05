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
     * 房间预约缓存key
     */
    public static final String ROOM_RESERVATION_KEY = "room:reservation:%s";

    /**
     * 每日推荐缓存key
     */
    public static final String RECOMMEND_KEY = "recommend_KEY:%s";

    /**
     * 歌单缓存key
     */
    public static final String PLAYLIST_KEY = "playlist_key:%s";

    /**
     * 用户歌单列表
     */
    public static final String PLAYLIST_USER_KEY = "playlist:user:%s";

    /**
     * 歌曲列表缓存key
     */
    public static final String PLAYLIST_LIST_KEY = "playlist:list:%s";
}
