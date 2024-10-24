package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间数据存储层
 */
@Mapper
public interface RoomMapper extends BaseMapper<RoomDO> {
}
