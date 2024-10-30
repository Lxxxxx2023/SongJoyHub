package com.lx.SongJoyHub.client.service.handler.filter;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.client.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.client.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.client.service.basic.chain.AbstractChainHandler;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import org.springframework.stereotype.Component;

@Component
public class MusicCreateParamNotNullChainFilter implements AbstractChainHandler<MusicCreateReqDTO> {


    @Override
    public void handler(MusicCreateReqDTO requestParam) {
        if(StrUtil.isEmpty(requestParam.getSongName()) || requestParam.getSongName() == null) {
            throw new ServiceException("歌曲名不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSinger()) || requestParam.getSinger() == null) {
            throw new ServiceException("演出者不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSongLanguage()) || requestParam.getSongLanguage() == null) {
            throw new ServiceException("歌曲语种不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSongAddress()) || requestParam.getSongAddress() == null) {
            throw new ServiceException("歌曲存放位置不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getLyric()) || requestParam.getLyric() == null) {
            throw new ServiceException("歌词存放位置不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getCategory()) || requestParam.getCategory() == null) {
            throw new ServiceException("分类不能为空");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.MUSIC_CREATE_KEY.name();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
