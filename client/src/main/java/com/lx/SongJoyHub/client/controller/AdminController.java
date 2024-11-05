package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dao.mapper.AdminMapper;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.AdminLoginRespDTO;
import com.lx.SongJoyHub.client.dto.resp.AdminPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.MemberLoginRespDTO;
import com.lx.SongJoyHub.client.service.AdminService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制层
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * 管理员登陆
     */
    @PostMapping("/login")
    public Result<AdminLoginRespDTO> login(@RequestBody AdminLoginReqDTO requestParam) {
        return Results.success(adminService.login(requestParam));
    }

    /**
     * 店长添加管理员
     */
    @PostMapping("/add")
    public Result<Void> saveAdmin(@RequestBody AdminAddReqDTO requestParam) {
        adminService.saveAdmin(requestParam);
        return Results.success();
    }

    /**
     * 查询所有员工
     */
    @GetMapping("/page-query")
    public Result<List<AdminPageQueryRespDTO>> pageQueryAdmin(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return Results.success(adminService.pageQueryAdmin(page, pageSize));
    }

    /**
     * 多条件查询 员工信息
     */
    @PostMapping("/multiple-query")
    public Result<List<AdminPageQueryRespDTO>> multipleQueryAdmin(@RequestBody AdminMultipleQueryReqDTO requestParam) {
        return Results.success(adminService.multipleQueryAdmin(requestParam));
    }

    /**
     * 上升权限
     */
    @PostMapping("/uplevel")
    public Result<Void> upAdminLevel(@RequestBody AdminUpLevelReqDTO requestParam) {
        adminService.upAdminLevel(requestParam);
        return Results.success();
    }
    /**
     * 降低权限
     */
    @PostMapping("/downlevel")
    public Result<Void> downAdminLevel(@RequestBody AdminDownAdminLevelReqDTO requestParam) {
        adminService.downAdminLevel(requestParam);
        return Results.success();
    }
}
