package com.lx.SongJoyHub.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.SongJoyHub.client.common.constant.SystemConstant;
import com.lx.SongJoyHub.client.common.context.UserInfoDTO;
import com.lx.SongJoyHub.client.common.enums.UserRoleEnum;
import com.lx.SongJoyHub.client.dao.entity.MemberDO;
import com.lx.SongJoyHub.client.dao.mapper.MemberMapper;
import com.lx.SongJoyHub.client.dto.req.MemberLoginReqDTO;
import com.lx.SongJoyHub.client.dto.req.MemberRegisterReqDTO;
import com.lx.SongJoyHub.client.dto.resp.MemberLoginRespDTO;
import com.lx.SongJoyHub.client.service.MemberService;
import com.lx.SongJoyHub.client.util.JWTUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 会员业务实现层
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, MemberDO> implements MemberService {
    @Value("${levelId}")
    private Long levelId;

    private final MemberMapper memberMapper;

    @Override
    public void register(MemberRegisterReqDTO requestParam) {
        // 参数校验
        if (StrUtil.isBlank(requestParam.getName())) {
            throw new ServiceException("用户名不能未空 且不能有空格");
        }
        Pattern pattern = Pattern.compile(SystemConstant.PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(requestParam.getPassword());
        if (!matcher.matches()) {
            throw new ServiceException("密码格式错误");
        }
        Pattern phonePattern = Pattern.compile(SystemConstant.PHONE_PATTERN);
        Matcher phone = phonePattern.matcher(requestParam.getPhone());
        if (!phone.matches()) {
            throw new ServiceException("手机号码格式输入错误");
        }
        // 保存
        MemberDO memberDO = BeanUtil.copyProperties(requestParam, MemberDO.class);
        memberDO.setLevelId(levelId);
        try {
            memberMapper.insert(memberDO);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("该手机号已注册");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public MemberLoginRespDTO login(MemberLoginReqDTO requestParam) {
        if (StrUtil.isBlank(requestParam.getPhone()) || StrUtil.isBlank(requestParam.getPassword())) {
            throw new ServiceException("密码或手机号格式错误");
        }
        LambdaQueryWrapper<MemberDO> queryWrapper = Wrappers.lambdaQuery(MemberDO.class)
                .eq(MemberDO::getPhone, requestParam.getPhone())
                .eq(MemberDO::getPassword, requestParam.getPassword());
        MemberDO memberDO = memberMapper.selectOne(queryWrapper);
        if (memberDO == null) {
            throw new ServiceException("不存在该用户 ！！！");
        }
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userId(memberDO.getId().toString())
                .userName(memberDO.getNick())
                .userRole(UserRoleEnum.user)
                .build();
        String accessToken = JWTUtil.generateToken(userInfoDTO);
        return MemberLoginRespDTO.builder().accessToken(accessToken).build();
    }
}
