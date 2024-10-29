package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryAllRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryReviewRespDTO;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
     * 管理员查看房间审核信息
     */
    @PostMapping("/query/room-review")
    public Result<List<RoomQueryReviewRespDTO>> findRoomReview(@RequestBody RoomQueryReviewReqDTO requestParam) {
        return Results.success(roomService.findRoomReview(requestParam));
    }

    /**
     * 管理员审核房间
     */
    @PostMapping("/review")
    public Result<Void> reviewRoom(@RequestBody RoomReviewReqDTO requestParam) {
        roomService.reviewRoom(requestParam);
        return Results.success();
    }
    /**
     * 查看房间
     */
    @PostMapping("/query/room")
    public Result<List<RoomQueryAllRespDTO>> findRoom(@RequestBody RoomQueryReqDTO requestParam) {
        return Results.success(roomService.findRoom(requestParam));
    }

    /**
     * 修改房间信息
     */
    @PostMapping("/update/info")
    public Result<Void> updateRoomInfo(@RequestBody RoomUpdateInfoReqDTO requestParam) {
        roomService.updateRoomInfo(requestParam);
        return Results.success();
    }

    /**
     * 下线房间
     */
    @PostMapping("/offline}")
    public Result<Void> offLineRoom(@RequestBody RoomOffLineReqDTO requestParam) {
        roomService.offLineRoom(requestParam);
        return Results.success();
    }

    /**
     * 上线房间
     */
    @PostMapping("/onLine")
    public Result<Void> onLineRoom(@RequestBody RoomOnLineReqDTO requestParam) {
        roomService.onLineRoom(requestParam);
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
}
