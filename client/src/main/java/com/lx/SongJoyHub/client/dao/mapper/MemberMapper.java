package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.MemberDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface MemberMapper extends BaseMapper<MemberDO> {

    /**
     * 扣减用户余额
     * @param money 扣减金额
     * @return 是否成功
     */
    int decrementBalance(@Param("money") BigDecimal money,@Param("id") Long id);

    /**
     * 返还用户订单金额
     * @param money 金额
     * @param id 用户id
     */
    int returnBalance(@Param("money") BigDecimal money,@Param("id") Long id);
}
