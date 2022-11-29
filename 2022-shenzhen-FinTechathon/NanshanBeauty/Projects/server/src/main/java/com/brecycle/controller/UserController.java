package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.config.shiro.JWTConfig;
import com.brecycle.config.shiro.JwtTokenUtil;
import com.brecycle.entity.dto.CustomerRegistParam;
import com.brecycle.entity.dto.EntRegistParam;
import com.brecycle.entity.dto.UserInfo;
import com.brecycle.entity.dto.UserLoginParam;
import com.brecycle.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户模块
 *
 * @author cmgun
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    Response<UserInfo> login(@RequestBody @ApiParam(value = "参数", required = true) UserLoginParam param, HttpServletResponse response) throws Exception {
        UserInfo userInfo = userService.login(param.getUserName(), param.getPassword());
        response.setHeader(JWTConfig.tokenHeader, userInfo.getToken());
        return Response.success("登录成功", userInfo);
    }

    @ApiOperation("用户信息")
    @PostMapping("/getInfo")
    Response<UserInfo> getInfo(HttpServletRequest request) throws Exception {
        UserInfo userInfo = userService.getInfo(request);
        return Response.success("查询成功", userInfo);
    }

    @ApiOperation("用户信息")
    @PostMapping("/logout")
    Response<UserInfo> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 理论来说jwt不需要登出，但还是消除下缓存的信息
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        userService.logout(userName);
        return Response.success("登出成功");
    }

    @ApiOperation("消费者注册")
    @PostMapping("/customerRegist")
    Response<UserInfo> customerRegist(@RequestBody @ApiParam(value = "参数", required = true) CustomerRegistParam param) throws Exception {
        userService.customerRegist(param);
        return Response.success("注册成功");
    }

    @ApiOperation("企业注册")
    @PostMapping("/entRegist")
    Response<UserInfo> entRegist(@RequestBody @ApiParam(value = "参数", required = true) EntRegistParam param) throws Exception {
        userService.entRegist(param);
        return Response.success("注册成功");
    }
}
