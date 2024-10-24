package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.service.SongReviewService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 歌曲审批控制类
 */
@RestController
@RequestMapping("/api/song-review")
@RequiredArgsConstructor
public class SongReviewController {

    private final SongReviewService songReviewService;
    /**
     * 歌曲审批
     */
    @PostMapping("/examine")
    public Result<Void> songExamine(@RequestBody SongReviewReqDTO requestParam) {
        songReviewService.songExamine(requestParam);
        return Results.success();
    }
}
