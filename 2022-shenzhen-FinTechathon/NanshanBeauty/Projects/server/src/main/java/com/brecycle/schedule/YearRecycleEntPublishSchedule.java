package com.brecycle.schedule;

import com.brecycle.entity.Trade;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.YearRecycleEntDTO;
import com.brecycle.enums.TradeType;
import com.brecycle.mapper.BatteryMapper;
import com.brecycle.mapper.TradeMapper;
import com.brecycle.mapper.UserMapper;
import com.brecycle.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 年度回收商积分派发
 *
 * @author cmgun
 */
@Slf4j
@Component
public class YearRecycleEntPublishSchedule {

    @Autowired
    PointService pointService;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 每年12.31号触发，扫描已经过期的竞价合约
     */
    @Scheduled(cron = "0 0 0 31 12 * ")
    public void process() {
        // 查询到期交易
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        String startTime = currentYear + "-01-01";
        String endTime = currentYear + "-12-31";
        List<YearRecycleEntDTO> entLists = batteryMapper.selectYearRecycleEntInfo(endTime, startTime);
        BigDecimal totalKah = entLists.stream().map(YearRecycleEntDTO::getTotalKah).reduce(BigDecimal::add).get();
        log.info("年度回收商积分派发失败，totalKah:{}", totalKah);
        for (YearRecycleEntDTO dto : entLists) {
            try {
                User recycleEnt = userMapper.selectById(dto.getOwnerId());
                pointService.yearRecycleEntPublish(recycleEnt, totalKah, dto.getTotalKah());
            } catch (Exception e) {
                log.error("年度回收商积分派发失败，企业id:{}", dto.getOwnerId(), e);
            }
        }
    }
}
