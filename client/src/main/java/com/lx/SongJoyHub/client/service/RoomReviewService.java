package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;

/**
 * 房间审核业务逻辑层
 */
public interface RoomReviewService extends IService<RoomReviewDO> {

    /**
     * 审核创建新房间
     * @param requestParam 审核创建新房间请求参数
     */
    void examineSaveRoom(RoomReviewReqDTO requestParam);

    /**
     * 审核更新房间
     * @param requestParam 审核更新房间请求参数
     */
    void examineUpdateRoom(RoomReviewReqDTO requestParam);

    /**
     * 审核删除房间
     * @param requestParam 审核删除房间
     */
    void examineDeleteRoom(RoomReviewReqDTO requestParam);
}
