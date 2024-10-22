package com.lx.SongJoyHub.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.SongJoyHub.client.dao.entity.MemberDO;
import com.lx.SongJoyHub.client.dto.req.MemberLoginReqDTO;
import com.lx.SongJoyHub.client.dto.req.MemberRegisterReqDTO;
import com.lx.SongJoyHub.client.dto.resp.MemberLoginRespDTO;


/**
 * 会员业务逻辑层
 */
public interface MemberService extends IService<MemberDO> {

    /**
     * 会员注册
     * @param requestParam 会员创建请求参数
     */
    void register(MemberRegisterReqDTO requestParam);

    /**
     * 会员登录
     * @param requestParam 会员登录参数
     */
    MemberLoginRespDTO login(MemberLoginReqDTO requestParam);

}
