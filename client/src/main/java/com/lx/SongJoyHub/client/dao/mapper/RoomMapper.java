package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 房间数据存储层
 */
@Mapper
public interface RoomMapper extends BaseMapper<RoomDO> {
    /**
     * 多条件更新房间信息
     */
    int updateRoom(@Param("room") RoomDO room);
}
