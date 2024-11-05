package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
import com.lx.SongJoyHub.framework.result.Result;

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
     * 修改房间信息
     * @param requestParam 修改房间请求参数
     */
    void updateRoom(RoomUpdateReqDTO requestParam);

    /**
     * 删除房间信息
     * @param requestParam 删除房间信息请求参数
     */
    void deleteRoom(RoomDeleteReqDTO requestParam);

    /**
     * 预约房间
     * @param requestParam 预约房间请求参数
     */
    void reservationRoom(RoomReservationReqDTO requestParam);

    /**
     * 取消预约房间
     * @param requestParam 取消预约房间请求参数
     */
    void cancelReservationRoom(RoomCancelReservationReqDTO requestParam);

    /**
     * 分页查看房间信息
     * @param page 页码
     * @param pageSize 页大小
     * @return 房间信息
     */
    List<RoomQueryRespDTO> pageQueryRoom(Integer page, Integer pageSize);

    /**
     * 多条件分页查询 房间信息
     * @param requestParam 请求参数
     * @return 房间信息
     */
    List<RoomQueryRespDTO> fuzzyInquiryRoom(RoomFuzzyInquiryReqDTO requestParam);

    /**
     * 滚动查询
     * @return 返回结果
     */
    List<RoomQueryRespDTO> rollQueryRoom(Long maxId, Integer pageSize);

    /**
     * 多条件分页滚动查询
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<RoomQueryRespDTO> multipleQueryRoom(RoomMultipleQueryReqDTO requestParam);

}
