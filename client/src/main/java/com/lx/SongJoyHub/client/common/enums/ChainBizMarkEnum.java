package com.lx.SongJoyHub.client.common.enums;

/**
 * 责任链类型枚举
 */
public enum ChainBizMarkEnum {
    /**
     * 验证歌曲创建参数是否正确
     */
    MUSIC_CREATE_KEY,
    /**
     * 校验房间创建是否正确
     */
    ROOM_CREATE_KEY,

    /**
     * 校验活动创建参数是否正确
     */
    ACTIVITY_CREATE_KEY;
    @Override
    public String toString() {
        return this.name();
    }
}
