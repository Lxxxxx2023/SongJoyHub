package com.lx.SongJoyHub.music.controller;

import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import com.lx.SongJoyHub.music.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.music.serivce.SongService;
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
}
