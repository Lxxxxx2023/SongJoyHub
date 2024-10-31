package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dao.entity.ActivityDO;
import com.lx.SongJoyHub.client.dao.mapper.ActivityMapper;
import com.lx.SongJoyHub.client.dto.req.ActivityCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.ActivityPageQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.QueryCanPartakeActivityReqDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryCanPartakeRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryCanPartakeResultRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryRespDTO;
import com.lx.SongJoyHub.client.mq.event.ActivityDelayEvent;
import com.lx.SongJoyHub.client.mq.producer.ActivityDelayExecutorStatusProducer;
import com.lx.SongJoyHub.client.service.ActivityService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.client.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 活动业务逻辑实现层
 */
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, ActivityDO> implements ActivityService {

    private final ChainHandlerContext chainHandlerContext;

    private final ActivityMapper activityMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final ActivityDelayExecutorStatusProducer activityDelayExecutorStatusProducer;

    private final static String USER_CAN_PARTAKE_LUA_PATH = "lua.user_can_partake";
    @Override
    public void createActivity(ActivityCreateReqDTO requestParam) {
        // 校验参数
        chainHandlerContext.handler(ChainBizMarkEnum.ACTIVITY_CREATE_KEY.name(), requestParam);
        // 插入数据库
        ActivityDO activityDO = BeanUtil.toBean(requestParam, ActivityDO.class);
        activityDO.setActivityStatus(1); // 设为有效
        activityMapper.insert(activityDO);
        // 存入缓存中
        ActivityQueryRespDTO activityQueryRespDTO = BeanUtil.toBean(activityDO, ActivityQueryRespDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(activityQueryRespDTO, false, true);
        RedisUtil.convertHash(
                String.format(RedisConstant.ACTIVITY_KEY, activityQueryRespDTO.getActivityId())
                , stringRedisTemplate
                , cacheTargetMap
                , String.valueOf(activityQueryRespDTO.getValidEndTime().getTime() / 1000)
        );
        // 发送mq 修改活动结束时 活动状态
        activityDelayExecutorStatusProducer.sendMessage(ActivityDelayEvent.builder()
                .activityId(activityDO.getActivityId())
                .activityStatus(0)
                .delayTime(activityDO.getValidEndTime().getTime())
                .build());
    }

    @Override
    public List<ActivityPageQueryRespDTO> pageQueryActivity(ActivityPageQueryReqDTO requestParam) {
        List<ActivityDO> activityDOS = activityMapper.pageQueryActivity(requestParam);
        return activityDOS.stream().map(each -> BeanUtil.toBean(each, ActivityPageQueryRespDTO.class)).toList();
    }

    // 根据用户信息获取能参与的活动 TODO 有时间就重构吧
    @Override
    public ActivityQueryCanPartakeResultRespDTO getCanPartakeActivity(QueryCanPartakeActivityReqDTO requestParam) {
        // 查询数据库
        Date now = DateUtil.date();
        LambdaQueryWrapper<ActivityDO> queryWrapper = Wrappers.lambdaQuery(ActivityDO.class)
                .eq(ActivityDO::getActivityStatus, 1)
                .ge(ActivityDO::getValidEndTime, now)
                .le(ActivityDO::getValidStartTime, now);
        List<ActivityDO> activityDOS = activityMapper.selectList(queryWrapper);
        List<ActivityDO> reliefLList = new ArrayList<>();
        List<ActivityDO> grantList = new ArrayList<>();
        AtomicReference<BigDecimal> discountAmount = new AtomicReference<>(BigDecimal.ZERO);
        Integer level = UserContext.getUser().getLevel();
        // 不符合开闭原则 不利用扩展
        activityDOS.forEach(each -> {
            JSONObject receiveRule = JSONObject.parseObject(each.getReceiveRule());
            if(each.getActivityStatus() == 0) {
                AtomicInteger flag = getAtomicInteger(requestParam, receiveRule, level);
                if(flag.get() == 1){
                    reliefLList.add(each);
                    // 统计优惠金额
                    JSONObject rewardContent = JSONObject.parseObject(each.getRewardContent());
                    Integer type = rewardContent.getInteger("type");
                    Double amount = rewardContent.getDouble("amount");
                    BigDecimal amountDecimal = BigDecimal.valueOf(amount);
                    if(type == 1) { // 折扣
                        discountAmount.set(requestParam.getAmount().multiply(amountDecimal));
                    }else if(type == 0){ // 满减
                        discountAmount.set(requestParam.getAmount().subtract(amountDecimal));
                    }
                }
            }else {
                AtomicInteger flag = getAtomicInteger(requestParam, receiveRule, level);
                if(flag.get() == 1) grantList.add(each);
            }
        });
        return ActivityQueryCanPartakeResultRespDTO.builder()
                .reliefLList(reliefLList.stream().map(each -> BeanUtil.toBean(each, ActivityQueryCanPartakeRespDTO.class)).toList())
                .grantList(grantList.stream().map(each -> BeanUtil.toBean(each, ActivityQueryCanPartakeRespDTO.class)).toList())
                .discount_amount(discountAmount.get())
                .build();
    }

    private static AtomicInteger getAtomicInteger(QueryCanPartakeActivityReqDTO requestParam, JSONObject jsonObject, Integer level) {
        AtomicInteger flag = new AtomicInteger(1);
        if(jsonObject.getInteger("level")!= null && jsonObject.getInteger("level") > level) {
            flag.set(0);
        }
        if(jsonObject.getInteger("timeCount") != null && jsonObject.getInteger("timeCount") > requestParam.getTimeCount()) {
            flag.set(0);
        }
        return flag;
    }

}
