package com.lx.SongJoyHub.client.service.basic.chain;
import org.springframework.core.Ordered;

/**
 * 责任链
 */
public interface MusicAbstractChainHandler<T> extends Ordered {
    /**
     * 执行处理逻辑
     * @param requestParma 处理实体类
     */
    void handler(T requestParma);

    /**
     * @return 责任链分组标识
     */
    String mark();
}
