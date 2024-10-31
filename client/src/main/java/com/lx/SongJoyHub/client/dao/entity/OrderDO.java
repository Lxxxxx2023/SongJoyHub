package com.lx.SongJoyHub.client.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDO  {

    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 券金额
     */
    private BigDecimal couponAmount;
    /**
     * 预约记录id
     */
    private Long reservationId;
    /**
     * 该订单涉及的活动
     */
    private String activityId;

    /**
     * 活动总优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 结算状态 2. 支付成功 1.未支付 3. 取消
     */
    private int orderStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}