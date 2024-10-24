package com.lx.SongJoyHub.client.service.basic.chain;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 责任链组件上下文
 */
@Component
public final class MusicChainHandlerContext<T> implements ApplicationContextAware, CommandLineRunner {
    /**
     * 应用上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 责任链组件处理器容器
     */
    private final Map<String, List<MusicAbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();

    /**
     * 责任链处理
     *
     * @param mark         责任链分组标识
     * @param requestParam 请求参数
     */
    public void handler(String mark, T requestParam) {
        List<MusicAbstractChainHandler> musicAbstractChainHandlers = abstractChainHandlerContainer.get(mark);
        if (musicAbstractChainHandlers == null) {
            throw new RuntimeException(String.format("[%s] 该责任链标识不存在", mark));
        }
        musicAbstractChainHandlers.forEach(chainFilter -> chainFilter.handler(requestParam));
    }

    @Override
    public void run(String... args) throws Exception {

        // 从Spring上下文中讲使用责任链处理器 获取出来
        Map<String, MusicAbstractChainHandler> chainFilterMap = applicationContext.getBeansOfType(MusicAbstractChainHandler.class);

        // 分组加入责任链上下文中
        chainFilterMap.forEach((beanName, bean) -> {
            List<MusicAbstractChainHandler> chainFilterList = abstractChainHandlerContainer.getOrDefault(bean.mark(), new ArrayList<>());
            chainFilterList.add(bean);
            abstractChainHandlerContainer.put(bean.mark(), chainFilterList);
        });

        // 责任链排序
        abstractChainHandlerContainer.forEach((mark, unsortedChainHandlerList) -> {
            unsortedChainHandlerList.sort(Comparator.comparing(Ordered::getOrder));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
