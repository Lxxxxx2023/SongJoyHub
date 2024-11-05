package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.SongMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.SongReviewReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.SongReviewQueryDiffRespDTO;
import com.lx.SongJoyHub.client.service.SongReviewService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 歌曲审批控制类
 */
@RestController
@RequestMapping("/api/song-examine")
@RequiredArgsConstructor
public class SongReviewController {

    private final SongReviewService songReviewService;

    /**
     * 查看未进行的审核
     */
    @GetMapping("/page-query")
    public Result<List<SongReviewPageQueryRespDTO>> pageQuerySongReview(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return Results.success(songReviewService.pageQuerySongReview(page - 1, pageSize));
    }

    /**
     * 多条件查询 审核情况
     */
    @PostMapping("/multiple-query")
    public Result<List<SongReviewPageQueryRespDTO>> multipleQuerySongReview(@RequestBody SongMultipleQueryReqDTO requestParam) {
        return Results.success(songReviewService.multipleQuerySongReview(requestParam));
    }

    /**
     * 根据id查询歌曲审核情况
     */
    @GetMapping("/query-review-info")
    public Result<SongReviewQueryDiffRespDTO> querySongReviewDiff(@RequestParam("id") Long id) {
        return Results.success(songReviewService.querySongReviewDiff(id));
    }
    /**
     * 审批新建歌曲
     */
    @PostMapping("/save")
    public Result<Void> examineSongSave(@RequestBody SongReviewReqDTO requestParam) {
        songReviewService.examineSaveMusic(requestParam);
        return Results.success();
    }

    /**
     * 审批删除歌曲
     */
    @PostMapping("/delete")
    public Result<Void> examineSongDelete(@RequestBody SongReviewReqDTO requestParam) {
        songReviewService.examineDeleteMusic(requestParam);
        return Results.success();
    }

    /**
     * 审批更新歌曲
     */
    @PostMapping("/update")
    public Result<Void> examineSongUpdate(@RequestBody SongReviewReqDTO requestParam) {
        songReviewService.examineUpdateMusic(requestParam);
        return Results.success();
    }
}
