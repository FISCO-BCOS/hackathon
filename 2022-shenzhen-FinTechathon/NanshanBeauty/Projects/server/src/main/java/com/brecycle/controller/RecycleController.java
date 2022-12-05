package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.config.shiro.JWTConfig;
import com.brecycle.config.shiro.JwtTokenUtil;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.*;
import com.brecycle.mapper.UserMapper;
import com.brecycle.service.RecycleService;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 回收模块
 *
 * @author cmgun
 */
@Api(tags = "回收模块")
@RestController
@RequestMapping("recycle")
public class RecycleController {

    @Autowired
    RecycleService recycleService;
    @Autowired
    UserMapper userMapper;

    @ApiOperation("回收申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping(value ="/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response apply(@ApiParam(value = "参数", required = true) RecycleApplyParam param,
                   @ApiParam(value = "上传的⽂件") @RequestParam(value = "file", required = false) MultipartFile file,
                   HttpServletRequest request) throws Exception {
        List<String> batteryIds = Lists.newArrayList();
        if (file != null) {
            // 解析excel文件，如果有的话
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                // 读取单元格内容
                String batteryId = StringUtils.trimToEmpty(row.getCell(0).getStringCellValue());
                batteryIds.add(batteryId);
            }
        }
        if (StringUtils.isNotBlank(param.getBatteryId())) {
            batteryIds.add(param.getBatteryId());
        }
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        recycleService.apply(batteryIds, param, userName);
        return Response.success("提交成功");
    }

    @ApiOperation("查询交易列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/list")
    Response list(@RequestBody @ApiParam(value = "参数", required = true) TradeListParam param) {
        PageResult<TradeListDTO> result = recycleService.list(param);
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
        PageResult<TradeListDTO> result = recycleService.list(param);
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
        recycleService.bid(param);
        return Response.success("提交成功");
    }
}
