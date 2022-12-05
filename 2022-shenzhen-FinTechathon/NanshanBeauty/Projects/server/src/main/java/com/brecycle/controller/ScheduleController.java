package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.schedule.PointDealSchedule;
import com.brecycle.schedule.RecycleDealSchedule;
import com.brecycle.schedule.SecondUsedDealSchedule;
import com.brecycle.schedule.YearRecycleEntPublishSchedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给特定系统进行topic消息发送
 *
 * @author cmgun
 */
@Slf4j
@Api(tags = "定时器手动执行模块")
@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    RecycleDealSchedule recycleDealSchedule;
    @Autowired
    SecondUsedDealSchedule secondUsedDealSchedule;
    @Autowired
    PointDealSchedule pointDealSchedule;
    @Autowired
    YearRecycleEntPublishSchedule yearRecycleEntPublishSchedule;


    @ApiOperation("回收交易到期")
    @PostMapping("/recycle/deal")
    Response recycleDeal() {
        recycleDealSchedule.process();
        return Response.success("执行成功");
    }

    @ApiOperation("梯次利用交易到期")
    @PostMapping("/secondUsed/deal")
    Response secondUsedDeal() {
        secondUsedDealSchedule.process();
        return Response.success("执行成功");
    }

    @ApiOperation("积分交易到期")
    @PostMapping("/point/deal")
    Response pointDeal() {
        pointDealSchedule.process();
        return Response.success("执行成功");
    }

    @ApiOperation("年度回收商积分派发到期")
    @PostMapping("/point/yearRecycleEntPublish")
    Response yearRecycleEntPublish() {
        yearRecycleEntPublishSchedule.process();
        return Response.success("执行成功");
    }
}
