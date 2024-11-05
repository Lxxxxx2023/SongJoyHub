package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dao.entity.UpdateSongReviewBO;
import com.lx.SongJoyHub.client.dto.req.SongMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewPageQueryRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 歌曲审批业务存储层
 */
@Mapper
public interface SongReviewMapper extends BaseMapper<SongReviewDO> {

    /**
     * 更新审核信息
     */
    int updateSongReview(@Param("req") UpdateSongReviewBO updateSongReviewBO);

    /**
     * 分页查询 歌曲审核信息
     */
    List<SongReviewPageQueryRespDTO> pageQuerySongReview(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 多条件分页查询歌曲审核信息
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<SongReviewPageQueryRespDTO> multipleQuerySongReview(@Param("req") SongMultipleQueryReqDTO requestParam);
}
