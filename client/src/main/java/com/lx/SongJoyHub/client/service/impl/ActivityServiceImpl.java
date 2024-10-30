package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson2.JSON;
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
import com.lx.SongJoyHub.client.dto.resp.ActivityPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryCanPartakeRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryRespDTO;
import com.lx.SongJoyHub.client.mq.event.ActivityDelayEvent;
import com.lx.SongJoyHub.client.mq.producer.ActivityDelayExecutorStatusProducer;
import com.lx.SongJoyHub.client.service.ActivityService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.client.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.*;
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
                , String.valueOf(activityQueryRespDTO.getValidEndTime().getTime() / 1000));
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

    // 根据用户信息获取能参与的活动
    @Override
    public List<ActivityQueryCanPartakeRespDTO> getCanPartakeActivity() {
        List<ActivityQueryCanPartakeRespDTO> results = new ArrayList<>();
        // 查询缓存
        String userCanCanPartakeKey = String.format(RedisConstant.CAN_PARTAKE_KEY, UserContext.getUserId());
        Set<String> members = stringRedisTemplate.opsForSet().members(userCanCanPartakeKey);
        if(members != null && !members.isEmpty()) {
            members.stream().map(each -> JSON.parseObject(each, ActivityQueryCanPartakeRespDTO.class)).forEach(results::add);
        }else {
            // 查询数据库
            Date now = DateUtil.date();
            LambdaQueryWrapper<ActivityDO> queryWrapper = Wrappers.lambdaQuery(ActivityDO.class)
                    .eq(ActivityDO::getActivityStatus, 1)
                    .ge(ActivityDO::getValidEndTime, now)
                    .le(ActivityDO::getValidStartTime, now)
                    .orderByAsc(ActivityDO::getValidEndTime);
            List<ActivityDO> activityDOS = activityMapper.selectList(queryWrapper);
            Integer userLevel = UserContext.getUser().getLevel();
            AtomicReference<Integer> flag = new AtomicReference<>(0);
            activityDOS.forEach(each -> {
                JSONObject receiveRule = JSONObject.parseObject(each.getReceiveRule());
                // 目前暂定为这些条件 如果时间充足的话就解决吧
                Integer level = receiveRule.getInteger("level");
                if (level != null && userLevel < level) {
                    flag.set(1);
                }
//            Integer limit = receiveRule.getInteger("limit"); // 参加这个活动的次数
                if (flag.get() == 1) {
                    results.add(BeanUtil.toBean(each, ActivityQueryCanPartakeRespDTO.class));
                }
            });
            // 创建缓存
            List<String> cacheList = new ArrayList<>(results.stream().map(JSON::toJSONString).toList());
            List<String> keys = List.of(userCanCanPartakeKey);
            cacheList.add(cacheList.get(cacheList.size() - 1));
            DefaultRedisScript<Void> buildLuaScript = Singleton.get((USER_CAN_PARTAKE_LUA_PATH), () -> {
                DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(USER_CAN_PARTAKE_LUA_PATH)));
                redisScript.setResultType(Void.class);
                return redisScript;
            });
            stringRedisTemplate.execute(buildLuaScript, keys,cacheList.toArray());
        }
        return results;
    }

}
