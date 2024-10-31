package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.ActivityDO;
import com.lx.SongJoyHub.client.dto.req.ActivityCreateReqDTO;
import com.lx.SongJoyHub.client.dto.req.ActivityPageQueryReqDTO;
import com.lx.SongJoyHub.client.dto.req.QueryCanPartakeActivityReqDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityPageQueryRespDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityQueryCanPartakeResultRespDTO;

import java.util.List;

/**
 * 活动业务逻辑层
 */
public interface ActivityService extends IService<ActivityDO> {

    /**
     * 创建活动
     * @param requestParam 创建活动请求参数
     */
    void createActivity(ActivityCreateReqDTO requestParam);

    /**
     * 分页查询活动
     * @param requestParam 分页查询请求参数
     * @return 活动返回结果
     */
    List<ActivityPageQueryRespDTO> pageQueryActivity(ActivityPageQueryReqDTO requestParam);

    /**
     * 获取当前用户能够参与的活动
     * @return 能够参与的活动
     */
    ActivityQueryCanPartakeResultRespDTO getCanPartakeActivity(QueryCanPartakeActivityReqDTO requestParam);

}
