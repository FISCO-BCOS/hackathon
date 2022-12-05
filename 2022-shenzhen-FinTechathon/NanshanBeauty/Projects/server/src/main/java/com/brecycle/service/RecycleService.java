package com.brecycle.service;

import com.brecycle.entity.Trade;
import com.brecycle.entity.dto.*;

import java.util.List;

/**
 * 回收处理
 */
public interface RecycleService {

    /**
     * 回收申请
     * @param batteryIds
     * @param param
     * @param currentUserName
     */
    void apply(List<String> batteryIds, RecycleApplyParam param, String currentUserName) throws Exception;

    /**
     * 交易查询
     * @param param
     * @return
     */
    PageResult<TradeListDTO> list(TradeListParam param);

    /**
     * 竞价交易
     * @param param
     */
    void bid(TradeParam param);

    /**
     * 到期交易
     * @param trade
     */
    void deal(Trade trade);
}
