package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单业务层
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
