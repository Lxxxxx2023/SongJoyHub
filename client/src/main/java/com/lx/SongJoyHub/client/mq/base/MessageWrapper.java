package com.lx.SongJoyHub.client.mq.base;

import lombok.*;
import java.io.Serializable;

/**
 * 消息体包装器
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@RequiredArgsConstructor
public final class MessageWrapper<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息发送keys
     */
    @NonNull
    private String keys;

    /**
     * 消息发送体
     */
    @NonNull
    private T message;

    /**
     * 消息发送时间
     */
    private Long timestamp = System.currentTimeMillis();
}
