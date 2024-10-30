package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 房间控制层
 */
@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    /**
     * 新增房间
     */
    @PostMapping("/create")
    public Result<Void> createRoom(@RequestBody RoomCreateReqDTO requestParam) {
        roomService.createRoom(requestParam);
        return Results.success();
    }

    /**
     * 修改房间信息
     */
    @PostMapping("/update")
    public Result<Void> updateRoom(@RequestBody RoomUpdateReqDTO requestParam) {
        roomService.updateRoom(requestParam);
        return Results.success();
    }

    /**
     * 删除房间
     */
    @PostMapping("/delete")
    public Result<Void> deleteRoom(@RequestBody RoomDeleteReqDTO requestParam) {
        roomService.deleteRoom(requestParam);
        return Results.success();
    }
    /**
     * 预约房间
     */
    @PostMapping("/reservation")
    public Result<Void> reservationRoom(@RequestBody RoomReservationReqDTO requestParam) {
        roomService.reservationRoom(requestParam);
        return Results.success();
    }
    //TODO 取消预约
}
