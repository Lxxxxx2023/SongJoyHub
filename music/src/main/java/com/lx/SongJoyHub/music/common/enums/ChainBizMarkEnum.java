package com.lx.SongJoyHub.music.common.enums;

import lombok.Getter;

/**
 * 责任链类型枚举
 */
public enum ChainBizMarkEnum {
    /**
     * 验证歌曲创建参数是否正确
     */
    MUSIC_CREATE_KEY;

    @Override
    public String toString() {
        return this.name();
    }
}