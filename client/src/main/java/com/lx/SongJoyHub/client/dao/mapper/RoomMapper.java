package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.RoomDO;
import com.lx.SongJoyHub.client.dto.req.RoomFuzzyInquiryReqDTO;
import com.lx.SongJoyHub.client.dto.req.RoomMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.RoomQueryRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间数据存储层
 */
@Mapper
public interface RoomMapper extends BaseMapper<RoomDO> {
    /**
     * 多条件更新房间信息
     */
    int updateRoom(@Param("room") RoomDO room);

    /**
     * 分页查看房间信息
     *
     * @param page     页码
     * @param pageSize 页大小
     * @return 房间信息
     */
    List<RoomQueryRespDTO> pageQueryRoom(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 多条件分页查询
     *
     * @param requestParam 请求参数
     * @return 房间信息
     */
    List<RoomQueryRespDTO> fuzzyInquiryRoom(@Param("param") RoomFuzzyInquiryReqDTO requestParam);

    /**
     * 提交审核时更新房间状态
     */
    int updateRoomStatus(@Param("id") Long id);

    /**
     * 审核完成 修改房间状态
     */
    int restoreRoomStatus(@Param("id") Long id);

    /**
     * 滚动查询房间
     * @param maxId 最大房间id
     * @param pageSize 数据大小
     * @return 返回值
     */
    List<RoomQueryRespDTO> rollQueryRoom(@Param("maxId") Long maxId, @Param("pageSize") Integer pageSize);

    /**
     * 多条件滚动查询
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<RoomQueryRespDTO> multipleQueryRoom(@Param("req") RoomMultipleQueryReqDTO requestParam);
}
