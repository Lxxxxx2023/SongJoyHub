package com.lx.SongJoyHub.client.service.handler.filter;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dto.req.ActivityCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import org.springframework.stereotype.Component;

/**
 * 校验活动创建请求参数是否为空
 */
@Component
public class ActivityCreateParamNotNullChainFilter implements AbstractChainHandler<ActivityCreateReqDTO> {

    @Override
    public void handler(ActivityCreateReqDTO requestParam) {
        if(requestParam.getActivityName() == null || StrUtil.isEmpty(requestParam.getActivityName())) {
            throw new ServiceException("创建活动时，活动名不能为空");
        }
        if(requestParam.getReceiveRule() == null || StrUtil.isEmpty(requestParam.getReceiveRule())) {
            throw new ServiceException("创建活动时，参加规则不能为空");
        }
        if(requestParam.getRewardContent() == null || StrUtil.isEmpty(requestParam.getRewardContent())) {
            throw new ServiceException("创建活动时，奖励内容不能为空");
        }
        if(requestParam.getValidStartTime() == null) {
            throw new ServiceException("活动开始时间不能为空");
        }
        if(requestParam.getValidEndTime() == null) {
            throw new ServiceException("活动结束时间不能为空");
        }

    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.ACTIVITY_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
