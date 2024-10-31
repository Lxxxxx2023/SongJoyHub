package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.common.enums.ReviewTypeEnum;
import com.lx.SongJoyHub.client.dao.entity.*;
import com.lx.SongJoyHub.client.dao.mapper.*;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 房间业务逻辑实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomDO> implements RoomService {

    private final ChainHandlerContext chainHandlerContext;

    private final RoomMapper roomMapper;

    private final RoomReservationMapper roomReservationMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final RoomReviewMapper roomReviewMapper;

    private final OrderMapper orderMapper;

    @Override
    public void createRoom(RoomCreateReqDTO requestParam) {
        // 校验参数
        chainHandlerContext.handler(ChainBizMarkEnum.ROOM_CREATE_KEY.name(), requestParam);
        RoomDO roomDO = BeanUtil.toBean(requestParam, RoomDO.class);
        // 创建审核任务
        String roomJson = JSON.toJSONString(roomDO, SerializerFeature.WriteNonStringValueAsString);
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                .cause("新建房间")
//                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(roomJson)
                .type(ReviewTypeEnum.INSERT.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }

    @Override
    public void updateRoom(RoomUpdateReqDTO requestParam) {
        // 创建审核任务
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if (roomDO == null) {
            throw new ServiceException("该房间不存在 无法进行修改！");
        }
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                //                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(JSON.toJSONString((BeanUtil.toBean(requestParam,RoomDO.class)),SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(roomDO,SerializerFeature.WriteNonStringValueAsString))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.UPDATE.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }

    @Override
    public void deleteRoom(RoomDeleteReqDTO requestParam) {
        // 创建审核任务
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if (roomDO == null) {
            throw new ServiceException("该房间不存在 无法进行删除！");
        }
        RoomDO oldSongDO = BeanUtil.copyProperties(roomDO, RoomDO.class);
        roomDO.setRoomStatus(3); //删除
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
//                .committerId(Long.valueOf(UserContext.getUserId()))
//                .committerName(UserContext.getUser().getUserName())
                .committerName("lx")
                .committerId(1L)
                .nowData(JSON.toJSONString(roomDO,SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(oldSongDO,SerializerFeature.WriteNonStringValueAsString))
                .cause(requestParam.getCause())
                .type(ReviewTypeEnum.DELETE.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }
    @Override
    @Transactional
    public void reservationRoom(RoomReservationReqDTO requestParam) {
        //setup1: 检查预订时间是否冲突
        DateTime startTime = DateUtil.beginOfHour(requestParam.getStartTime());
        DateTime endTime = DateUtil.endOfHour(requestParam.getEndTime());
        String reservationRoomKey = String.format(RedisConstant.ROOM_RESERVATION_KEY, requestParam.getRoomId());
        String startTimeCache = String.valueOf(startTime.getTime());
        String endTimeCache = String.valueOf(endTime.getTime());
        Set<String> reservationRange = stringRedisTemplate.opsForZSet().rangeByScore(reservationRoomKey, startTime.getTime(), endTime.getTime());
        if (reservationRange != null && !reservationRange.isEmpty()) {
            throw new ServiceException("本房间预定的时间段发生冲突");
        }
        // step2: 保存到数据库
        Long userId = Long.valueOf(UserContext.getUserId());
        RoomReservationDO roomReservationDO = RoomReservationDO.builder()
                .roomId(requestParam.getRoomId())
                .userId(userId)
                .startTime(startTime)
                .endTime(endTime)
                .status(1)
                .build();
        try{
            roomReservationMapper.insert(roomReservationDO);
        }catch (DuplicateKeyException e) {
            throw new ServiceException("房间预约失败");
        }

        // step3: 更新缓存  这里其实要添加过期时间避免大key问题
        List<String> strings = Collections.singletonList(reservationRoomKey);
        List<String> times = List.of(userId.toString(), startTimeCache, userId.toString(), endTimeCache);
        String luaScript = "redis.call('ZADD',KEYS[1],ARGV[1],ARGV[2])" +
                "redis.call('ZADD',KEYS[1],ARGV[3],ARGV[4])";
        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), strings, times.toArray());

        // step4:生成订单
        OrderDO orderDO = OrderDO.builder()
                .totalAmount(requestParam.getTotalAmount())
                .payableAmount(requestParam.getPayableAmount())
                .activityId(requestParam.getActivityIds())
                .reservationId(roomReservationDO.getReservationId())
                .userId(Long.valueOf(UserContext.getUserId()))
                .roomId(requestParam.getRoomId())
                .couponId(requestParam.getCouponId())
                .couponAmount(requestParam.getCouponAmount())
                .discountAmount(requestParam.getDiscountAmount())
                .orderStatus(1) // 创建状态
                .build();
        orderMapper.insert(orderDO);
        // TODO step5: 添加用户参加活动记录
        // TODO step6: 发放奖励
    }

    @Override
    public void cancelReservationRoom(RoomCancelReservationReqDTO requestParam) {
        // 获取该用户未取消的订单
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getId, requestParam.getOrderId())
                .lt(OrderDO::getOrderStatus,3)
                .eq(OrderDO::getUserId, UserContext.getUserId());
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if(orderDO == null) {
            throw new ServiceException("该订单不存在或已取消");
        }
        // 获取预约情况
        RoomReservationDO roomReservationDO = roomReservationMapper.selectById(orderDO.getReservationId());
        DateTime latestCancelTime = DateUtil.offsetHour(roomReservationDO.getStartTime(), -5);
        Date now = new Date();
        if(now.after(latestCancelTime)) {
            throw new ServiceException("在预约时间前5个小时无法取消");
        }
        roomReservationDO.setStatus(0);
        roomReservationMapper.insert(roomReservationDO);

        // 更新缓存
        String reservationRoomKey = String.format(RedisConstant.ROOM_RESERVATION_KEY, roomReservationDO.getRoomId());
        DateTime startTime = DateUtil.beginOfHour(roomReservationDO.getStartTime());
        DateTime endTime = DateUtil.endOfHour(roomReservationDO.getEndTime());
        stringRedisTemplate.opsForZSet().removeRange(reservationRoomKey,startTime.getTime() ,endTime.getTime());

        // TODO 恢复发放的积分和优惠券以及用户支付的钱
    }
}
