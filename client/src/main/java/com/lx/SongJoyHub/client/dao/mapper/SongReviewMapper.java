package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌曲审批业务存储层
 */
@Mapper
public interface SongReviewMapper extends BaseMapper<SongReviewDO> {
}
