package com.media.utils;

import java.math.BigInteger;

/**
 * @author YZR
 * @date 2020/11/6 11:23
 * 公共属性
 */

public class Constants {

    // 系统当前时间---s
    public static BigInteger CURRENT_TIME = BigInteger.valueOf(System.currentTimeMillis() / 1000);

    // 短信服务
    public static final String SMS_REGION_ID = "ID0";
    public static final String SMS_ACCESS_KEY_ID = "密钥1";
    public static final String SMS_ACCESS_KEY_SECRET = "密钥2";
    public static final String SMS_REQUEST_DOMAIN = "URL";
    public static final String SMS_REQUEST_VERSION = "time";
    public static final String SMS_REQUEST_ACTION = "服务";
    public static final String SMS_SIGN_NAME = "机构";
    public static final String SMS_LOGIN = "ID1";
    public static final String SMS_REGISTER = "ID2";

    // pbkdf2加密
    static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int SALT_SIZE = 64;
    static final int HASH_SIZE = 64;
    static final int PBKDF2_ITERATIONS = 1323;

    // Jwt生成token的key
    static final String JWT_KET = "xdlianrong-MaskView";
    static final long TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    public static final String SERVER_ERROR = "服务器异常";

    // loginByPwd
    public static final String LOGIN_SUCCESS = "登陆成功";
    public static final String LOGIN_PWD_ERROR = "用户名或密码错误";
    public static final String LOGIN_NOT_EXIST = "未注册,请用手机验证码登录!";
    // sendCode
    public static final String LOGIN_CODE_SUCCESS = "登录验证码发送成功";
    public static final String LOGIN_CODE_FAIL = "登录验证码发送失败";
    public static final String REGISTER_CODE_SUCCESS = "注册验证码发送成功";
    public static final String REGISTER_CODE_FAIL = "注册验证码发送失败";
    public static final int REGISTER_CODE_TAG = 0;
    public static final int LOGIN_CODE_TAG = 1;
    public static final String LOGIN_CODE_NOT_EXIST = "未注册,请先注册!";
    // loginByCode
    public static final String LOGIN_CODE_ERROR = "验证码错误";


}
