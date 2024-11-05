package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.AdminDO;
import com.lx.SongJoyHub.client.dto.req.AdminMultipleQueryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.AdminPageQueryRespDTO;
import com.lx.SongJoyHub.framework.result.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  管理员控制层
 */
@Mapper
public interface AdminMapper extends BaseMapper<AdminDO> {
    /**
     * 分页查询管理员信息
     * @param page 页码
     * @param pageSize 页大小
     * @return 返回值
     */
    List<AdminPageQueryRespDTO> pageQueryAdmin(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 多条件查询管理员
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<AdminPageQueryRespDTO> multipleQueryAdmin(@Param("req") AdminMultipleQueryReqDTO requestParam);

    /**
     * 升级权限
     * @param id 请求参数
     */
    int upAdminLevel(@Param("id") Long id);

    /**
     * 降低权限
     * @param id 请求参数
     */
    int downAdminLevel(@Param("id") Long id);
}
