package com.media.controller;

import com.media.model.LoginModel;
import com.media.pojo.CommonResult;
import com.media.pojo.OtherCode;
import com.media.pojo.RegisterCode;
import com.media.pojo.User;
import com.media.service.OtherCodeService;
import com.media.service.RegisterCodeService;
import com.media.service.SmsSendService;
import com.media.service.UserService;
import com.media.utils.Constants;
import com.media.utils.JwtUtils;
import com.media.utils.Pbkdf2WithSha256;
import com.media.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    SmsSendService smsSendService;
    @Autowired
    RegisterCodeService registerCodeService;
    @Autowired
    OtherCodeService otherCodeService;

    @GetMapping("/test")
    @ResponseBody
    public void rrr(HttpServletResponse response) {
        try {
            //response.addHeader("Auth","sdfsdfsdfsd");
            response.sendRedirect("https://www.baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录---密码登录
     *
     * @param phoneNumber
     * @param password
     * @return
     */
    @PostMapping("/loginByPwd")
    @ResponseBody
    public CommonResult loginByPwd(@RequestParam(name = "phoneNumber") BigInteger phoneNumber,
                                   @RequestParam(name = "password") String password) {
        User user;
        try {
            // 登录, 先判断用户名密码是否正确
            user = userService.getUserInfoByPhoneNumber(phoneNumber);
            if (user != null) {
                String salt = user.getSalt();
                // pbkdf2加密
                String key = Pbkdf2WithSha256.getPBKDF2(password, salt);
                if (key.equals(user.getKey())) {
                    // 用户名密码正确,生成token
                    String token = JwtUtils.createToken(user.getUid(), user.getPhoneNumber());
                    LoginModel loginResponse = new LoginModel(user.getUid(), user.getPhoneNumber(), user.getUserName(),
                            user.getHeadView(), user.getBackgroundWall(), user.getBirth(), user.getSex(), token);
                    return new CommonResult(true, Constants.LOGIN_SUCCESS, loginResponse);
                } else {
                    return new CommonResult(false, Constants.LOGIN_PWD_ERROR, null);
                }
            } else {
                return new CommonResult(false, Constants.LOGIN_NOT_EXIST, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, Constants.SERVER_ERROR, null);
        }
    }

    /**
     * 发送验证码---Android端登录
     *
     * @param phoneNumber
     * @return
     */
    @PostMapping("/sendCode")
    @ResponseBody
    public CommonResult sendCode(@RequestParam(name = "phoneNumber") BigInteger phoneNumber) {
        System.out.println("发送验证码模块，正在发送验证码......");
        User user;
        try {
            user = userService.getUserInfoByPhoneNumber(phoneNumber);
            int code = SmsUtils.getCode();
            if (user == null) {
                // 注册发验证码
                if (smsSendService.sendCode(phoneNumber, Constants.SMS_REGISTER, code)) {
                    // 注册验证码发送成功, 入库
                    registerCodeService.insertRegisterCode(new RegisterCode(phoneNumber, code, Constants.CURRENT_TIME));
                    return new CommonResult(true, Constants.REGISTER_CODE_SUCCESS, Constants.REGISTER_CODE_TAG);
                } else {
                    return new CommonResult(false, Constants.REGISTER_CODE_FAIL);
                }
            } else {
                System.out.println("进入");
                // 登录发验证码
                if (smsSendService.sendCode(phoneNumber, Constants.SMS_LOGIN, code)) {
                    // 登录验证码发送成功, 入库
                    if (otherCodeService.isExist(phoneNumber)) {
                        System.out.println("有该手机号");
                        otherCodeService.updateCode(new OtherCode(phoneNumber, code, Constants.CURRENT_TIME));
                    } else {
                        System.out.println("没有该手机号");
                        otherCodeService.insertOtherCode(new OtherCode(phoneNumber, code, Constants.CURRENT_TIME));
                    }
                    return new CommonResult(true, Constants.LOGIN_CODE_SUCCESS, Constants.LOGIN_CODE_TAG);
                } else {
                    return new CommonResult(false, Constants.LOGIN_CODE_FAIL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, Constants.SERVER_ERROR);
        }
    }


    /**
     * 发送验证码---Windows端注册
     *
     * @param phoneNumber
     * @return
     */
    @PostMapping("/sendRegisterCode")
    @ResponseBody
    public CommonResult sendRegisterCode(@RequestParam(name = "phoneNumber") BigInteger phoneNumber) {
        User user;
        try {
            user = userService.getUserInfoByPhoneNumber(phoneNumber);
            if (user == null) {
                int code = SmsUtils.getCode();
                // 注册发验证码
                if (smsSendService.sendCode(phoneNumber, Constants.SMS_REGISTER, code)) {
                    // 注册验证码发送成功, 入库
                    registerCodeService.insertRegisterCode(new RegisterCode(phoneNumber, code, Constants.CURRENT_TIME));
                    return new CommonResult(true, Constants.REGISTER_CODE_SUCCESS, Constants.REGISTER_CODE_TAG);
                } else {
                    return new CommonResult(false, Constants.REGISTER_CODE_FAIL);
                }
            } else {
                return new CommonResult(false, "该账号已注册过, 请直接登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, Constants.SERVER_ERROR);
        }
    }

    /**
     * 发送验证码---Windows端登录
     *
     * @param phoneNumber
     * @return
     */
    @PostMapping("sendLoginCode")
    @ResponseBody
    public CommonResult sendLoginCode(@RequestParam(name = "phoneNumber") BigInteger phoneNumber) {
        User user;
        try {
            user = userService.getUserInfoByPhoneNumber(phoneNumber);
            if (user != null) {
                int code = SmsUtils.getCode();
                // 登录发验证码
                if (smsSendService.sendCode(phoneNumber, Constants.SMS_LOGIN, code)) {
                    // 登录验证码发送成功, 入库
                    otherCodeService.insertOtherCode(new OtherCode(phoneNumber, code, Constants.CURRENT_TIME));
                    return new CommonResult(true, Constants.LOGIN_CODE_SUCCESS, Constants.LOGIN_CODE_TAG);
                } else {
                    return new CommonResult(false, Constants.LOGIN_CODE_FAIL);
                }
            } else {
                return new CommonResult(false, Constants.LOGIN_CODE_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, Constants.SERVER_ERROR);
        }
    }

    /**
     * 登录---验证码登录
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    @PostMapping("/loginByCode")
    @ResponseBody
    public CommonResult loginByCode(@RequestParam(name = "phoneNumber") BigInteger phoneNumber,
                                    @RequestParam(name = "code") int code) {
        int codeSql;
        User user;
        try {
            codeSql = otherCodeService.getHerCode(phoneNumber);
            if (codeSql == code) {
                // 生成token
                user = userService.getUserInfoByPhoneNumber(phoneNumber);
                String token = JwtUtils.createToken(user.getUid(), user.getPhoneNumber());
                LoginModel loginResponse = new LoginModel(user.getUid(), user.getPhoneNumber(), user.getUserName(),
                        user.getHeadView(), user.getBackgroundWall(), user.getBirth(), user.getSex(), token);
                return new CommonResult(true, Constants.LOGIN_SUCCESS, loginResponse);
            } else {
                return new CommonResult(false, Constants.LOGIN_CODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, Constants.SERVER_ERROR);
        }
    }

    @PostMapping("/verifyToken")
    @ResponseBody
    public CommonResult verifyToken(ServletRequest servletRequest) {
        try {
            String token = ((HttpServletRequest) servletRequest).getHeader("Authorization");
            token = token.replaceAll("Bearer ", "");
            System.out.println(token);
            if (StringUtils.isNoneBlank(token)) {
                int verify = JwtUtils.verify(token);
                if (verify == 2) {
                    return new CommonResult(false, "token过期,请重新登陆", null);
                } else {
                    return new CommonResult(true, "提取到uid", verify);
                }
            } else {
                return new CommonResult(false, "token无效", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, "token无效-异常", null);
        }
    }
}
