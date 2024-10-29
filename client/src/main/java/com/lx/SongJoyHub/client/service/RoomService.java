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
     * 修改房间信息
     * @param requestParam 修改房间请求参数
     */
    void updateRoom(RoomUpdateReqDTO requestParam);

    /**
     * 删除房间信息
     * @param requestParam 删除房间信息请求参数
     */
    void deleteRoom(RoomDeleteReqDTO requestParam);
}
