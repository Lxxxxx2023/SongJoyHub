package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.RoomReviewMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewQueryDiffRespDTO;
import com.lx.SongJoyHub.client.service.RoomReviewService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 房间审核处理类
 */
@RestController
@RequestMapping("/api/room-examine")
@RequiredArgsConstructor
public class RoomReviewController {

    private final RoomReviewService roomReviewService;

    /**
     * 分页查询房间信息
     */
    @GetMapping("/page-query")
    public Result<List<RoomReviewPageQueryRespDTO>> pageQueryRoomReview(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return Results.success(roomReviewService.pageQueryRoomReview(page,pageSize));
    }

    /**
     * 多条件查询房间信息
     */
    @PostMapping("/multiple-query")
    public Result<List<RoomReviewPageQueryRespDTO>> multipleQueryRoomReview(@RequestBody RoomReviewMultipleQueryReqDTO requestParam) {
        return Results.success(roomReviewService.multipleQueryRoomReview(requestParam));
    }
    /**
     * 根据id查询房间审核信息
     */
    @GetMapping("/query-review-info")
    public Result<RoomReviewQueryDiffRespDTO> queryRoomReviewDiff(@RequestParam("id") Integer id) {
        return Results.success(roomReviewService.queryRoomReviewDiff(id));
    }
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
