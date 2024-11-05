package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
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
     * 分页查看房间
     */
    @GetMapping("/page")
    public Result<List<RoomQueryRespDTO>> pageQueryRoom(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return Results.success(roomService.pageQueryRoom(page,pageSize));
    }

    /**
     * 多条件分页查询房间
     */
    @PostMapping("/fuzzy-inquiry")
    public Result<List<RoomQueryRespDTO>> fuzzyInquiryRoom(@RequestBody RoomFuzzyInquiryReqDTO requestParam) {
        return Results.success(roomService.fuzzyInquiryRoom(requestParam));
    }

    /**
     * 滚动查询房间信息 -用户端
     */
    @PostMapping("/query")
    public Result<List<RoomQueryRespDTO>> rollQueryRoom(@RequestBody RoomRollQueryReqDTO requestParam) {
        return Results.success(roomService.rollQueryRoom(requestParam.getMaxId(), requestParam.getPageSize()));
    }

    /**
     * 多条件滚动查询 -用户端
     */
    @PostMapping("/multiple-query")
    public Result<List<RoomQueryRespDTO>> multipleQueryRoom(@RequestBody RoomMultipleQueryReqDTO requestParam) {
        return Results.success(roomService.multipleQueryRoom(requestParam));
    }
}
