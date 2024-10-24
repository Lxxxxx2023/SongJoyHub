package com.lx.SongJoyHub.music.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.music.dao.entity.SongReviewDO;
import com.lx.SongJoyHub.music.dto.req.SongReviewReqDTO;

/**
 * 歌曲审批业务层
 */
public interface SongReviewService extends IService<SongReviewDO> {
    /**
     * 歌曲审批
     * @param requestParam 歌曲审批请求参数
     */
    void songExamine(SongReviewReqDTO requestParam);
}
