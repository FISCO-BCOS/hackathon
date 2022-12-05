package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.config.shiro.JWTConfig;
import com.brecycle.config.shiro.JwtTokenUtil;
import com.brecycle.entity.dto.*;
import com.brecycle.service.BatteryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 电池接口
 *
 * @author cmgun
 */
@Api(tags = "电池管理")
@RestController
@RequestMapping("battery")
public class BatteryController {

    @Autowired
    BatteryService batteryService;

    @ApiOperation("手动新增单个电池")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/add")
    Response add(@RequestBody @ApiParam(value = "参数", required = true) BatteryInfoParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        param.setProductorUserName(userName);
        batteryService.add(param);
        return Response.success("提交成功");
    }

    @ApiOperation("查询电池流转信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/traceInfo")
    Response traceInfo(@RequestBody @ApiParam(value = "参数", required = true) TraceInfoParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        List<TraceInfoDTO> traceInfoDTOS = batteryService.getTraceInfo(param.getId(), userName);
        return Response.success("提交成功", traceInfoDTOS);
    }

    @ApiOperation("查询电池信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/list")
    Response list(@RequestBody @ApiParam(value = "参数", required = true) BatteryListParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        PageResult<BatteryListDTO> result = batteryService.batteryList(param, userName);
        return Response.success("查询成功", result);
    }
}
