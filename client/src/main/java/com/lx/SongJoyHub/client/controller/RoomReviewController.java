package com.lx.SongJoyHub.client.controller;


import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;
import com.lx.SongJoyHub.client.service.RoomReviewService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间审核处理类
 */
@RestController
@RequestMapping("/api/room-examine")
@RequiredArgsConstructor
public class RoomReviewController {
    private final RoomReviewService roomReviewService;

    /**
     * 审核新建房间
     */
    @PostMapping("/save")
    public Result<Void> examineSaveRoom(@RequestBody RoomReviewReqDTO requestParam) {
        roomReviewService.examineSaveRoom(requestParam);
        return Results.success();
    }

    /**
     * 审核更新房间信息
     */
    @PostMapping("/update")
    public Result<Void> examineUpdateRoom(@RequestBody RoomReviewReqDTO requestParam) {
        roomReviewService.examineUpdateRoom(requestParam);
        return Results.success();
    }

    /**
     * 审核删除房间信息
     */
    @PostMapping("/delete")
    public Result<Void> examineDeleteRoom(@RequestBody RoomReviewReqDTO requestParam) {
        roomReviewService.examineDeleteRoom(requestParam);
        return Results.success();
    }
}
