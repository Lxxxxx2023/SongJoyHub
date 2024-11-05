package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.AdminDO;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.AdminLoginRespDTO;
import com.lx.SongJoyHub.client.dto.resp.AdminPageQueryRespDTO;

import java.util.List;

/**
 * 管理业务逻辑层
 */
public interface AdminService extends IService<AdminDO> {

    /**
     * 管理员登录
     * @param requestParam 管理员登录请求参数
     * @return 登录token
     */
    AdminLoginRespDTO login(AdminLoginReqDTO requestParam);

    /**
     * 店长添加管理员
     * @param requestParam 请求参数
     */
    void saveAdmin(AdminAddReqDTO requestParam);

    /**
     * 分页查询管理员信息
     * @param page 页码
     * @param pageSize 页大小
     * @return 返回值
     */
    List<AdminPageQueryRespDTO> pageQueryAdmin(Integer page, Integer pageSize);

    /**
     * 多条件查询管理员
     * @param requestParam 请求参数
     * @return 返回值
     */
    List<AdminPageQueryRespDTO> multipleQueryAdmin(AdminMultipleQueryReqDTO requestParam);

    /**
     * 升级权限
     * @param requestParam 请求参数
     */
    void upAdminLevel(AdminUpLevelReqDTO requestParam);

    /**
     * 降低权限
     * @param requestParam 降低权限请求参数
     */
    void downAdminLevel(AdminDownAdminLevelReqDTO requestParam);

}
