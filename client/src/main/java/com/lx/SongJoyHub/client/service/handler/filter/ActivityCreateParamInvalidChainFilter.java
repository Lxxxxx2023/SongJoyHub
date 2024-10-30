package com.lx.SongJoyHub.client.service.handler.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dto.req.ActivityCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import org.springframework.stereotype.Component;

/**
 * 校验创建活动时，参数是否有效
 */
@Component
public class ActivityCreateParamInvalidChainFilter implements AbstractChainHandler<ActivityCreateReqDTO> {
    @Override
    public void handler(ActivityCreateReqDTO requestParma) {
        if (!JSONUtil.isTypeJSON(requestParma.getReceiveRule())) {
            throw new ServiceException("创建活动时，参加规则格式错误");
        }
        if(!JSONUtil.isTypeJSON(requestParma.getRewardContent())) {
            throw new ServiceException("创建活动时，奖励内容错误");
        }
        if(requestParma.getValidStartTime().before(DateUtil.date())) {
            throw new ServiceException("创建活动时，活动生效时间不能晚于当前时间");
        }
        if(requestParma.getValidEndTime().after(requestParma.getValidStartTime())) {
            throw new ServiceException("创建活动时，活动生效时间不能早于结束时间");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.ACTIVITY_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
