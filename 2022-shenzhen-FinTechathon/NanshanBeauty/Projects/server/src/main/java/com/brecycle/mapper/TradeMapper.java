package com.brecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.brecycle.entity.Trade;
import com.brecycle.entity.dto.TradeListParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cmgun
 */
@Mapper
public interface TradeMapper extends BaseMapper<Trade> {

    /**
     * 查询指定类型的过期竞价中交易
     * @param type
     * @return
     */
    List<Trade> selectExpireTrade(@Param("type") String type);

    IPage<Trade> selectTradeListByPage(IPage page, @Param("param") TradeListParam param);
}
