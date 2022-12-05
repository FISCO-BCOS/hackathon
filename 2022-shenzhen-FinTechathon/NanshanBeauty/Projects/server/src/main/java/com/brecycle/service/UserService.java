package com.brecycle.service;

import com.brecycle.entity.dto.CustomerRegistParam;
import com.brecycle.entity.dto.EntRegistParam;
import com.brecycle.entity.dto.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cmgun
 */
public interface UserService {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    UserInfo login(String userName, String password) throws Exception;

    /**
     * 获取用户信息
     * @return
     */
    UserInfo getInfo(HttpServletRequest request);

    /**
     * 登出
     */
    void logout(String userName);

    /**
     * 消费者注册
     * @param param
     */
    void customerRegist(CustomerRegistParam param) throws Exception;

    /**
     * 企业注册
     * @param param
     */
    void entRegist(EntRegistParam param) throws Exception;
}
