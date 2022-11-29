package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.config.shiro.JWTConfig;
import com.brecycle.config.shiro.JwtTokenUtil;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.*;
import com.brecycle.mapper.UserMapper;
import com.brecycle.service.PointService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author cmgun
 */
@Slf4j
@Api(tags = "积分模块")
@RestController
@RequestMapping("point")
public class PointTradeController {

    @Autowired
    PointService pointService;
    @Autowired
    UserMapper userMapper;

    @ApiOperation("初始化积分合约")
    @PostMapping("/init/deploy")
    Response deployContract() throws Exception {
        String addr = pointService.deployContract();
        log.info("初始化积分合约，发送结果:{}", addr);
        return Response.success("处理成功", addr);
    }

    @ApiOperation("初始化dao")
    @PostMapping("/init/dao")
    Response addDao() throws Exception {
        pointService.addDAO();
        return Response.success("处理成功");
    }

    @ApiOperation("初始化积分")
    @PostMapping("/init/total")
    Response initTotalPoint() throws Exception {
        pointService.initPoint();
        return Response.success("处理成功");
    }

    @ApiOperation("初始化指定数量积分")
    @PostMapping("/init/targetTotal")
    Response initTotalPoint(@RequestParam(value = "total1", required = false) BigDecimal value1, @RequestParam(value = "total2", required = false) BigDecimal value2) throws Exception {
        pointService.initPoint(value1, value2);
        return Response.success("处理成功");
    }

    @ApiOperation("查询积分记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/logs")
    Response getPointLogs(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        List<PointLogDTO> result = pointService.getPointLogs(userName);
        return Response.success("查询成功", result);
    }

    @ApiOperation("查询登录用户的积分")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/my")
    Response getMyPoint(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        BigDecimal result = pointService.getPoint(userName);
        return Response.success("查询成功", result);
    }

    @ApiOperation("积分交易申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping(value ="/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response apply(@ApiParam(value = "参数", required = true) PointTradeApplyParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        pointService.apply(param, userName);
        return Response.success("提交成功");
    }

    @ApiOperation("查询交易列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/list")
    Response list(@RequestBody @ApiParam(value = "参数", required = true) TradeListParam param) {
        PageResult<TradeListDTO> result = pointService.list(param);
        return Response.success("查询成功", result);
    }

    @ApiOperation("查询登录企业的交易列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/myList")
    Response myList(@RequestBody @ApiParam(value = "参数", required = true) TradeListParam param, HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        User current = userMapper.selectByUserName(userName);
        param.setMyId(current.getId());
        PageResult<TradeListDTO> result = pointService.list(param);
        return Response.success("查询成功", result);
    }

    @ApiOperation("提交竞价")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/bid")
    Response bid(@RequestBody @ApiParam(value = "参数", required = true) TradeParam param, HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        param.setBuyerUserName(userName);
        pointService.bid(param);
        return Response.success("提交成功");
    }
}
