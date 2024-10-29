package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 歌曲数据存储层
 */
@Mapper
public interface SongMapper extends BaseMapper<SongDO> {
    /**
     * 多条件更新歌曲信息
     */
    int updateSong(@Param("song") SongDO song);
}
