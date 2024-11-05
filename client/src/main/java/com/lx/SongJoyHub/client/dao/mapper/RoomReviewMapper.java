package com.lx.SongJoyHub.client.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.RoomReviewDO;
import com.lx.SongJoyHub.client.dao.entity.UpdateRoomReviewBO;
import com.lx.SongJoyHub.client.dto.req.RoomReviewMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomReviewPageQueryRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomReviewMapper extends BaseMapper<RoomReviewDO> {
    /**
     * 更新审核状态
     */
    int updateRoomReview(@Param("req") UpdateRoomReviewBO updateRoomReviewBO);

    /**
     * 分页查询房间审核情况
     * @param page 页码
     * @param pageSize 页大小
     * @return 返回值
     */
    List<RoomReviewPageQueryRespDTO> pageQueryRoomReview(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 多条件分页查询房间信息
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<RoomReviewPageQueryRespDTO> multipleQueryRoomReview(@Param("req") RoomReviewMultipleQueryReqDTO requestParam);
}
