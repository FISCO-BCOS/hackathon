package com.brecycle.schedule;

import com.brecycle.entity.Trade;
import com.brecycle.enums.TradeType;
import com.brecycle.mapper.TradeMapper;
import com.brecycle.service.RecycleService;
import com.brecycle.service.SecondUsedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 扫描竞价到期的梯次利用交易
 *
 * @author cmgun
 */
@Slf4j
@Component
public class SecondUsedDealSchedule {

    @Autowired
    SecondUsedService secondUsedService;
    @Autowired
    TradeMapper tradeMapper;

    /**
     * 每天1点触发，扫描已经过期的竞价合约
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void process() {
        // 查询到期交易
        List<Trade> tradeList = tradeMapper.selectExpireTrade(TradeType.SECOND_USED.getValue());
        log.info("当前到期的梯次利用交易数量为:{}", tradeList.size());
        for (Trade trade : tradeList) {
            try {
                secondUsedService.deal(trade);
            } catch (Exception e) {
                log.error("交易执行失败，交易编号:{}", trade.getId(), e);
            }
        }
    }
}
