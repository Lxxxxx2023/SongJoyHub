package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dto.req.SongMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewQueryDiffRespDTO;

import java.util.List;


/**
 * 歌曲审批业务层
 */
public interface SongReviewService extends IService<SongReviewDO> {

    /**
     * 查询未处理审批
     * @return 歌曲审批信息
     */
    List<SongReviewPageQueryRespDTO> pageQuerySongReview(Integer page, Integer pageSize);

    /**
     * 审批 上传歌曲信息
     * @param requestParam 上传歌曲请求参数
     */
    void examineSaveMusic(SongReviewReqDTO requestParam);

    /**
     * 审核 删除音乐
     * @param requestParam 删除音乐请求参数
     */
    void examineDeleteMusic(SongReviewReqDTO requestParam);

    /**
     * 审核 修改音乐
     * @param requestParam 修改音乐请求参数
     */
    void examineUpdateMusic(SongReviewReqDTO requestParam);

    /**
     * 多条件分页查询歌曲审核信息
     * @param requestParam 入参
     * @return 多条件分页查询歌曲审核信息 返回值
     */
    List<SongReviewPageQueryRespDTO> multipleQuerySongReview(SongMultipleQueryReqDTO requestParam);

    /**
     * 查询审核详情
     * @param id 审核记录id
     * @return 审核详情
     */
    SongReviewQueryDiffRespDTO querySongReviewDiff(Long id);
}
