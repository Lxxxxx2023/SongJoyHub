package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.RoomReservationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间预约数据持久层
 */
@Mapper
public interface RoomReservationMapper extends BaseMapper<RoomReservationDO> {
}
