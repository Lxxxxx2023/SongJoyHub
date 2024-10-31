package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.OrderPayReqDTO;
import com.lx.SongJoyHub.client.dto.req.QueryCanPartakeActivityReqDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryCanPartakeResultRespDTO;
import com.lx.SongJoyHub.client.service.ActivityService;
import com.lx.SongJoyHub.client.service.OrderService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制层
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final ActivityService activityService;

    // 获取用户能参加的活动
    @GetMapping("/able")
    public Result<ActivityQueryCanPartakeResultRespDTO> getCanPartakeActivity(QueryCanPartakeActivityReqDTO requestParam) {
        return Results.success(activityService.getCanPartakeActivity(requestParam));
    }

    //支付订单
    @PostMapping("/pay")
    public Result<Void> payOrder(@RequestBody OrderPayReqDTO requestParam) {
        orderService.payOrder(requestParam);
        return Results.success();
    }
}
