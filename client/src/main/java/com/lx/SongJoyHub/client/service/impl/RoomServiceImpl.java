package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.constant.RedisConstant;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.common.enums.OrderStatusEnum;
import com.lx.SongJoyHub.client.common.enums.ReviewTypeEnum;
import com.lx.SongJoyHub.client.common.enums.RoomReservationEnum;
import com.lx.SongJoyHub.client.dao.entity.*;
import com.lx.SongJoyHub.client.dao.mapper.*;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
import com.lx.SongJoyHub.client.mq.event.OrderTimeoutCancelEvent;
import com.lx.SongJoyHub.client.mq.producer.OrderTimeoutCancelProducer;
import com.lx.SongJoyHub.client.service.RoomService;
import com.lx.SongJoyHub.client.service.basic.chain.ChainHandlerContext;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import com.lx.SongJoyHub.framework.result.Result;
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

    private final OrderTimeoutCancelProducer orderTimeoutCancelProducer;

    private final MemberMapper memberMapper;

    @Override
    public void createRoom(RoomCreateReqDTO requestParam) {
        // 校验参数
        chainHandlerContext.handler(ChainBizMarkEnum.ROOM_CREATE_KEY.name(), requestParam);
        RoomDO roomDO = BeanUtil.toBean(requestParam, RoomDO.class);
        // 创建审核任务
        String roomJson = JSON.toJSONString(roomDO, SerializerFeature.WriteNonStringValueAsString);
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                .cause(requestParam.getCause())
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .nowData(roomJson)
                .type(ReviewTypeEnum.INSERT.getCode())
                .build();
        roomReviewMapper.insert(roomReviewDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(RoomUpdateReqDTO requestParam) {
        // 创建审核任务
        RoomDO roomDO = roomMapper.selectById(requestParam.getRoomId());
        if (roomDO == null) {
            throw new ServiceException("该房间不存在 无法进行修改！");
        }
        int update = roomMapper.updateRoomStatus(requestParam.getRoomId());
        if (!SqlHelper.retBool(update)) {
            throw new ServiceException("提交更新房间信息失败");
        }

        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .nowData(JSON.toJSONString((BeanUtil.toBean(requestParam, RoomDO.class)), SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(roomDO, SerializerFeature.WriteNonStringValueAsString))
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
        int update = roomMapper.updateRoomStatus(requestParam.getRoomId());
        if (!SqlHelper.retBool(update)) {
            throw new ServiceException("提交更新房间信息失败");
        }
        RoomDO oldSongDO = BeanUtil.copyProperties(roomDO, RoomDO.class);
        RoomReviewDO roomReviewDO = RoomReviewDO.builder()
                .committerId(Long.valueOf(UserContext.getUserId()))
                .committerName(UserContext.getUser().getUserName())
                .nowData(JSON.toJSONString(roomDO, SerializerFeature.WriteNonStringValueAsString))
                .originalData(JSON.toJSONString(oldSongDO, SerializerFeature.WriteNonStringValueAsString))
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
        DateTime dateTime = DateUtil.date();
        if(dateTime.isAfter(startTime)) {
            throw new ServiceException("不能预约过去的时间");
        }
        if(startTime.isAfter(endTime)) {
            throw new ServiceException("开始时间不能再结束时间之前");
        }
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
                .status(RoomReservationEnum.VALID.getCode())
                .build();
        try {
            roomReservationMapper.insert(roomReservationDO);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("房间预约失败");
        }

        // step3:生成订单
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
        OrderTimeoutCancelEvent orderTimeoutCancelEvent = OrderTimeoutCancelEvent.builder()
                .reservationId(roomReservationDO.getReservationId())
                .orderId(orderDO.getId())
                .delayTime(DateUtil.offsetMinute(dateTime, 5).getTime() - dateTime.getTime())
                .build();
        orderTimeoutCancelProducer.sendMessage(orderTimeoutCancelEvent);
        // step4: 更新缓存  这里其实要添加过期时间避免大key问题
        List<String> strings = Collections.singletonList(reservationRoomKey);
        List<String> times = List.of(startTimeCache, startTimeCache, endTimeCache, endTimeCache);
        String luaScript = "redis.call('ZADD',KEYS[1],ARGV[1],ARGV[2])" +
                "redis.call('ZADD',KEYS[1],ARGV[3],ARGV[4])";
        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), strings, times.toArray());
    }

    @Override
    @Transactional
    public void cancelReservationRoom(RoomCancelReservationReqDTO requestParam) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getId, requestParam.getOrderId())
                .lt(OrderDO::getOrderStatus, OrderStatusEnum.cancel.getCode())
                .eq(OrderDO::getUserId, UserContext.getUserId());
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if (orderDO == null) {
            throw new ServiceException("该订单不存在或已取消");
        }
        RoomReservationDO roomReservationDO = roomReservationMapper.selectById(orderDO.getReservationId());
        DateTime latestCancelTime = DateUtil.offsetHour(roomReservationDO.getStartTime(), -3);
        Date now = new Date();
        if (now.after(latestCancelTime)) {
            throw new ServiceException("在预约时间前3个小时无法取消");
        }
        LambdaUpdateWrapper<RoomReservationDO> updateWrapper = Wrappers.lambdaUpdate(RoomReservationDO.class)
                .eq(RoomReservationDO::getReservationId, orderDO.getReservationId())
                .set(RoomReservationDO::getStatus, RoomReservationEnum.CANCEL.getCode());
        roomReservationMapper.update(updateWrapper);
        LambdaUpdateWrapper<OrderDO> orderUpdateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                .eq(OrderDO::getId, orderDO.getId())
                .set(OrderDO::getOrderStatus, OrderStatusEnum.cancel.getCode());
        int update = orderMapper.update(orderUpdateWrapper);
        if(!SqlHelper.retBool(update)) {
            throw new ServiceException("取消订单失败");
        }
        String reservationRoomKey = String.format(RedisConstant.ROOM_RESERVATION_KEY, roomReservationDO.getRoomId());
        DateTime startTime = DateUtil.beginOfHour(roomReservationDO.getStartTime());
        DateTime endTime = DateUtil.endOfHour(roomReservationDO.getEndTime());
        stringRedisTemplate.opsForZSet().removeRangeByScore(reservationRoomKey, startTime.getTime(), endTime.getTime());
        if(orderDO.getOrderStatus() == OrderStatusEnum.pay.getCode()) {
            // TODO 恢复发放的积分和优惠券以及用户支付的钱
            memberMapper.returnBalance(orderDO.getPayableAmount(),orderDO.getUserId());
        }
        // 怎么判定用户是否能放歌
        // 用户在预约的房间登录获取到了该房间
    }
    @Override
    public List<RoomQueryRespDTO> pageQueryRoom(Integer page, Integer pageSize) {
        return roomMapper.pageQueryRoom(page - 1, pageSize);
    }

    @Override
    public List<RoomQueryRespDTO> fuzzyInquiryRoom(RoomFuzzyInquiryReqDTO requestParam) {
        requestParam.setPage(requestParam.getPage() - 1);
        return roomMapper.fuzzyInquiryRoom(requestParam);
    }

    @Override
    public List<RoomQueryRespDTO> rollQueryRoom(Long maxId, Integer pageSize) {
        // TODO 走缓存
        // 直接查询数据库
        return roomMapper.rollQueryRoom(maxId, pageSize);
    }

    @Override
    public List<RoomQueryRespDTO> multipleQueryRoom(RoomMultipleQueryReqDTO requestParam) {
        return roomMapper.multipleQueryRoom(requestParam);
    }

}
