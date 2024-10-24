package com.lx.SongJoyHub.music.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.music.dao.entity.SongDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌曲数据存储层
 */
@Mapper
public interface SongMapper extends BaseMapper<SongDO> {
}
