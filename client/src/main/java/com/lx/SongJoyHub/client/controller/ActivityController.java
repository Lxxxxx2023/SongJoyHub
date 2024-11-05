package com.lx.SongJoyHub.client.controller;

import com.lx.SongJoyHub.client.dto.req.ActivityCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.ActivityPageQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityPageQueryRespDTO;
import com.lx.SongJoyHub.client.service.ActivityService;
import com.lx.SongJoyHub.framework.result.Result;
import com.lx.SongJoyHub.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 活动控制层
 */
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 管理员发布活动
    @PostMapping("/create")
    public Result<Void> createActivity(@RequestBody ActivityCreateReqDTO requestParam) {
        activityService.createActivity(requestParam);
        return Results.success();
    }

    // 管理员分页查询活动
    @GetMapping("/pageQuery")
    public Result<List<ActivityPageQueryRespDTO>> pageQueryActivity(ActivityPageQueryReqDTO requestParam) {
        return Results.success(activityService.pageQueryActivity(requestParam));
    }

// 管理员创建和编辑发布活动， 活动应该分为主动参与和被动触发
// 举个具体的例子：国庆期间 预定房间便宜20元 这种情况肯定不是用户主动点击参加活动才能参加的，而是系统自动判定的
// 而用户主动去参与的活动应该是 用户积分去升级会员等级 ，用积分去兑换一些东西 而这些活动应该算到积分服务中去
// Q: 系统什么时候进行判定呢 A: 应该是在用户订单完成的时候
//   Q:如果活动是下单有折扣呢 A: 那我们先考虑能参加哪些活动 再创建订单
//    Q:如果活动判断时间较长 影响了用户的下单该怎么办 A: 提前判断哪些用户可以参加本次的活动
//     Q:怎么提前判断呢 A:预热
//      Q:那如果有用户突然满足条件了呢 A:找到触发的时机 进行加入
// Q:怎么进行判断呢 A: 用户信息加日期去判断
// Q:活动能叠加参与吗奖励怎么发放呢 能叠加参与 像积分 优惠券这种我们等到订单完成时再发放 像折扣我们在订单前进行处理

// 用户参加活动
//    @PostMapping("/partake")
//    public Result<Void> partakeActivity(@RequestBody ActivityPartakeReqDTO requestParam) {
//        activityService.partakeActivity(requestParam);
//        return Results.success();
//    }
}
