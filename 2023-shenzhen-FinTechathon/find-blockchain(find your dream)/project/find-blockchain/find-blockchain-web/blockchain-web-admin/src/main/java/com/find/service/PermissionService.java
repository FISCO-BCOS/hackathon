//package com.find.service;
//
///**
// * Description:spring-security框架之测试用户权限认证，该项目没有使用前后端分离，所以该类不用理会
// * Author: Su
// * Date: 2023/10/31
// */
//
//import com.find.pojo.LoginUser;
//import com.find.util.SecurityUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
///**
// * 自定义权限实现，ss取自SpringSecurity首字母
// */
////@Service("ss")
//public class PermissionService {
//    public boolean hasPer(String permission) throws Exception {
//        if (StringUtils.isBlank(permission)){
//            return true;
//        }
//        LoginUser loginUser = SecurityUtil.getLoginUser();
//        System.out.println("loginUser" + loginUser);
//        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
//            assert loginUser != null;
//            System.out.println("loginUser.getPermissions()" + loginUser.getPermissions());
//            return false;
//        }
//        return loginUser.getPermissions().contains(StringUtils.trim(permission));
//    }
//}
//
