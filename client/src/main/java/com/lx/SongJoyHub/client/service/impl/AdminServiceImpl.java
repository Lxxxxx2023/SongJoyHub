package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lx.SongJoyHub.client.common.context.UserContext;
import com.lx.SongJoyHub.client.common.context.UserInfoDTO;
import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;
import com.lx.SongJoyHub.client.dao.entity.AdminDO;
import com.lx.SongJoyHub.client.dao.mapper.AdminMapper;
import com.lx.SongJoyHub.client.dto.req.*;
import com.lx.SongJoyHub.client.dto.resp.AdminLoginRespDTO;
import com.lx.SongJoyHub.client.dto.resp.AdminPageQueryRespDTO;
import com.lx.SongJoyHub.client.service.AdminService;
import com.lx.SongJoyHub.client.util.JWTUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员业务实现层
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminDO> implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public AdminLoginRespDTO login(AdminLoginReqDTO requestParam) {
        if (StrUtil.isBlank(requestParam.getPhone()) || StrUtil.isBlank(requestParam.getPassword())) {
            throw new ServiceException("密码或手机号格式错误");
        }
        LambdaQueryWrapper<AdminDO> queryWrapper = Wrappers.lambdaQuery(AdminDO.class)
                .eq(AdminDO::getPhone, requestParam.getPhone())
                .eq(AdminDO::getPassword, requestParam.getPassword());
        AdminDO adminDO = adminMapper.selectOne(queryWrapper);

        if (adminDO == null) {
            throw new ServiceException("密码或手机号错误");
        }
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userId(adminDO.getId().toString())
                .userName(adminDO.getNick())
                .userRole(UserRoleEnum.ADMIN)
                .level(adminDO.getLevel())
                .build();
        String accessToken = JWTUtil.generateToken(userInfoDTO);
        return AdminLoginRespDTO.builder().accessToken(accessToken).build();
    }

    @Override
    public void saveAdmin(AdminAddReqDTO requestParam) {
        if(UserContext.getUser().getLevel() < 3) {
            throw new ServiceException("用户权限不足无法添加用户");
        }
        AdminDO adminDO = AdminDO.builder()
                .phone(requestParam.getPhone())
                .name(requestParam.getName())
                .level(requestParam.getLevel())
                .password("songjoyhub2024")
                .build();
        adminMapper.insert(adminDO);
    }

    @Override
    public List<AdminPageQueryRespDTO> pageQueryAdmin(Integer page, Integer pageSize) {
        return adminMapper.pageQueryAdmin(page - 1,pageSize);
    }

    @Override
    public List<AdminPageQueryRespDTO> multipleQueryAdmin(AdminMultipleQueryReqDTO requestParam) {
        requestParam.setPage(requestParam.getPage() - 1);
        return adminMapper.multipleQueryAdmin(requestParam);
    }

    @Override
    public void upAdminLevel(AdminUpLevelReqDTO requestParam) {
        if(UserContext.getUser().getLevel() < 3) {
            throw new ServiceException("用户权限不足");
        }
        AdminDO adminDO = adminMapper.selectById(requestParam.getId());
        if(adminDO == null) {
            throw new ServiceException("用户不存在无法升级权限");
        }
        if(adminDO.getLevel() == 3) {
            throw new ServiceException("已经是最高权限了");
        }
        int i = adminMapper.upAdminLevel(requestParam.getId());
        if(!SqlHelper.retBool(i)) {
            throw new ServiceException("提升权限失败 请刷新");
        }
    }

    @Override
    public void downAdminLevel(AdminDownAdminLevelReqDTO requestParam) {
        if(UserContext.getUser().getLevel() < 3) {
            throw new ServiceException("用户权限不足");
        }
        AdminDO adminDO = adminMapper.selectById(requestParam.getId());
        if(adminDO == null) {
            throw new ServiceException("用户不存在无法升级权限");
        }
        if(adminDO.getLevel() == 1) {
            throw new ServiceException("已经是最低权限了");
        }
        int i = adminMapper.downAdminLevel(requestParam.getId());
        if(!SqlHelper.retBool(i)) {
            throw new ServiceException("修改权限失败");
        }
    }
}
