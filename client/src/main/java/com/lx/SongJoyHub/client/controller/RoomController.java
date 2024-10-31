package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * 预约房间
     */
    // step1: 检验预约时间是否冲突
    // step2: 将预约情况保存到数据库
    // step3: 更新缓存
    // step4: 生成订单
    // Q:怎么生成订单 或者说生成订单需要什么东西
    // A: 房间id 总金额 应付金额 参加了哪些活动 用了哪些券 各优惠多少
    // 这里我们先不考虑用券的情况 只考虑活动
    // 活动这里我们应该分为三类 积分翻倍、发放优惠券、支付减免
    // 那对于订单付款金额，应该和支付的减免 和优惠券有关
    // 那在创建订单的时候，我们应该关注减免的活动和优惠券带来的优惠
    // 至于其他的就在支付订单完成后进行发放
    //Q:怎么获取减免活动和发放活动呢
    @PostMapping("/reservation")
    public Result<Void> reservationRoom(@RequestBody RoomReservationReqDTO requestParam) {
        roomService.reservationRoom(requestParam);
        return Results.success();
    }


    /**
     *取消预约房间
     */
    @PostMapping("/cancel")
    public Result<Void> cancelReservationRoom(@RequestBody RoomCancelReservationReqDTO requestParam) {
        roomService.cancelReservationRoom(requestParam);
        return Results.success();
    }
}
