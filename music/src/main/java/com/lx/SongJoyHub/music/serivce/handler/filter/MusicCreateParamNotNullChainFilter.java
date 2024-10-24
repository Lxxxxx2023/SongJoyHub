package com.lx.SongJoyHub.music.serivce.handler.filter;

import cn.hutool.core.util.StrUtil;
import com.lx.SongJoyHub.framework.exception.ServiceException;
import com.lx.SongJoyHub.music.common.enums.ChainBizMarkEnum;
import com.lx.SongJoyHub.music.dto.req.MusicCreateReqDTO;
import com.lx.SongJoyHub.music.serivce.basic.chain.MusicAbstractChainHandler;
import org.springframework.stereotype.Component;

@Component
public class MusicCreateParamNotNullChainFilter implements MusicAbstractChainHandler<MusicCreateReqDTO> {


    @Override
    public void handler(MusicCreateReqDTO requestParam) {
        if(StrUtil.isEmpty(requestParam.getSongName())) {
            throw new ServiceException("歌曲名不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSinger())) {
            throw new ServiceException("演出者不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSongLanguage())) {
            throw new ServiceException("歌曲语种不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getSongAddress())) {
            throw new ServiceException("歌曲存放位置不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getLyric())) {
            throw new ServiceException("歌词存放位置不能为空");
        }
        if(StrUtil.isEmpty(requestParam.getCategory())) {
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
