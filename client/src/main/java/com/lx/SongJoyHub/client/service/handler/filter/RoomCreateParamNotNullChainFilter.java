package com.lx.SongJoyHub.client.service.handler.filter;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dto.req.RoomCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import org.springframework.stereotype.Component;

/**
 * 验证参数是否为空
 */
@Component
public class RoomCreateParamNotNullChainFilter implements AbstractChainHandler<RoomCreateReqDTO> {
    @Override
    public void handler(RoomCreateReqDTO requestParma) {
        if(requestParma.getRoomType() == null) {
            throw new ServiceException("房间类型不能为空");
        }
        if(StrUtil.isEmpty(requestParma.getIntroduction()) || requestParma.getIntroduction() == null) {
            throw new ServiceException("房间简介不能为空");
        }
        if(requestParma.getPrice() == null) {
            throw new ServiceException("房间价格不能为空");
        }
        if(StrUtil.isEmpty(requestParma.getRoomName()) || requestParma.getRoomName() == null) {
            throw new ServiceException("房间名不能为空");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.ROOM_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
