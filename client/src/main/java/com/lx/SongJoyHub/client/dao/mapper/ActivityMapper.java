package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.ActivityDO;
import com.lx.SongJoyHub.client.dto.req.ActivityPageQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.ActivityPageQueryRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 活动存储层
 */
@Mapper
public interface ActivityMapper extends BaseMapper<ActivityDO> {

    /**
     * 分页查询活动
     * @param requestParam 请求参数
     * @return 分页查询活动返回值
     */
    List<ActivityDO> pageQueryActivity(@Param("requestParam")ActivityPageQueryReqDTO requestParam);
}
