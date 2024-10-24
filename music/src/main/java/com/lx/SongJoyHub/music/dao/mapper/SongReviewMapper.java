package com.lx.SongJoyHub.music.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.music.dao.entity.SongReviewDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌曲审批业务存储层
 */
@Mapper
public interface SongReviewMapper extends BaseMapper<SongReviewDO> {
}
