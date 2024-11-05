package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewQueryDiffRespDTO;

import java.util.List;

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

    /**
     * 分页查询房间审核情况
     * @param page 页码
     * @param pageSize 页大小
     * @return 房间审核情况
     */
    List<RoomReviewPageQueryRespDTO> pageQueryRoomReview(Integer page, Integer pageSize);

    /**
     * 查询房间审核情况 以对比的方式
     * @param id id
     * @return  对比的审核情况
     */
    RoomReviewQueryDiffRespDTO queryRoomReviewDiff(Integer id);

    /**
     * 多条件查询房间预约信息
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<RoomReviewPageQueryRespDTO> multipleQueryRoomReview(RoomReviewMultipleQueryReqDTO requestParam);
}
