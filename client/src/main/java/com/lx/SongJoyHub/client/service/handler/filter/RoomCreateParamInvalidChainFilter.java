package com.lx.SongJoyHub.client.service.handler.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dao.mapper.RoomMapper;
import com.lx.SongJoyHub.client.dto.req.RoomCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 验证创建房间参数是否合法
 */
@Component
@RequiredArgsConstructor
public class RoomCreateParamInvalidChainFilter implements AbstractChainHandler<RoomCreateReqDTO> {

    private final RoomMapper roomMapper;

    @Override
    public void handler(RoomCreateReqDTO requestParam) {
        // TODO 验证房间类型是否正确
        if (requestParam.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("房间价不能少于 0");
        }
        LambdaQueryWrapper<RoomDO> queryWrapper = Wrappers.lambdaQuery(RoomDO.class)
                .eq(RoomDO::getRoomName, requestParam.getRoomName())
                .ne(RoomDO::getRoomStatus,3);
        RoomDO roomDO = roomMapper.selectOne(queryWrapper);
        if(roomDO != null) {
            throw new ServiceException("房间名已存在");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.ROOM_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
