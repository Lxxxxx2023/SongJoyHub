package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicDeleteReqDTO;
import com.lx.SongJoyHub.client.dto.req.MusicUpdateReqDTO;
import com.lx.SongJoyHub.client.service.SongService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 音乐控制层
 */
@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {

    private final SongService songService;

    /**
     * 上传音乐
     */
    @PostMapping("/add")
    public Result<Void> addMuSic(@RequestBody MusicCreateReqDTO requestParam) {
        songService.addMusic(requestParam);
        return Results.success();
    }

    /**
     * 删除歌曲
     */
    @PostMapping("/delete")
    public Result<Void> deleteMusic(@RequestBody MusicDeleteReqDTO requestParam) {
        songService.deleteMusic(requestParam);
        return Results.success();
    }

    /**
     * 更新歌曲
     */
    @PostMapping("/update")
    public Result<Void> updateMusic(@RequestBody MusicUpdateReqDTO requestParam) {
        songService.updateMusic(requestParam);
        return Results.success();
    }
}
