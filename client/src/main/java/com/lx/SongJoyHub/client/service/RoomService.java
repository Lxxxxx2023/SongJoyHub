package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryAllRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryReviewRespDTO;

import java.util.List;

/**
 * 房间业务逻辑层
 */
public interface RoomService extends IService<RoomDO>{

    /**
     * 创建房间
     * @param requestParam 创建房间请求参数
     */
    void createRoom(RoomCreateReqDTO requestParam);

    /**
     * 预约房间
     * @param requestParam 预约房间请求参数
     */
    void reservationRoom(RoomReservationReqDTO requestParam);

    /**
     * 查找所有房间
     * @return 所有房间信息
     */
    List<RoomQueryAllRespDTO> findRoom(RoomQueryReqDTO requestParam);

    /**
     * 修改房间信息
     * @param requestParam 修改房间请求参数
     */
    void updateRoomInfo(RoomUpdateInfoReqDTO requestParam);

    /**
     * 下线房间
     * @param requestParam 房间id
     */
    void offLineRoom(RoomOffLineReqDTO requestParam);

    /**
     * 上线房间
     * @param requestParam 上线房间请求参数
     */
    void onLineRoom(RoomOnLineReqDTO requestParam);

    /**
     * 查询审核信息
     * @param requestParam 查询审核信息请求参数
     * @return 房间审核信息返回实体
     */
    List<RoomQueryReviewRespDTO> findRoomReview(RoomQueryReviewReqDTO requestParam);

    /**
     * 管理员审核房间
     * @param requestParam 审核房间请求参数
     */
    void reviewRoom(RoomReviewReqDTO requestParam);
}
