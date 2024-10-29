package com.lx.SongJoyHub.client.service.handler.filter;

import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dto.req.RoomCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 验证创建房间参数是否合法
 */
@Component
public class RoomCreateParamInvalidChainFilter implements AbstractChainHandler<RoomCreateReqDTO> {
    @Override
    public void handler(RoomCreateReqDTO requestParma) {
        // TODO 验证房间类型是否正确
        if (requestParma.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("房间价不能少于 0");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.ROOM_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
