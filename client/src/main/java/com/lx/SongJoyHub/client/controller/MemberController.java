package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.MemberLoginReqDTO;
import com.lx.SongJoyHub.client.dto.req.MemberRegisterReqDTO;
import com.lx.SongJoyHub.client.dto.resp.MemberLoginRespDTO;
import com.lx.SongJoyHub.client.service.MemberService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员控制层
 */
@RestController
@RequestMapping("/api/client/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody MemberRegisterReqDTO requestParam) {
        memberService.register(requestParam);
        return Results.success();
    }

    @PostMapping("/login")
    public Result<MemberLoginRespDTO> login(@RequestBody MemberLoginReqDTO requestParam) {
        return Results.success(memberService.login(requestParam));
    }
}
