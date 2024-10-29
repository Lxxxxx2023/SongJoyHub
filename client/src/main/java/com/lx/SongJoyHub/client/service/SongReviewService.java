package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewRespDTO;
import java.util.List;


/**
 * 歌曲审批业务层
 */
public interface SongReviewService extends IService<SongReviewDO> {

    /**
     * 查询未处理审批
     * @return 歌曲审批信息
     */
    List<SongReviewRespDTO> examineQueryUnprocessed();

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

}
