//package com.find.util;
//
//import com.find.pojo.LoginUser;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
///**
// * Description:
// * Author: Su
// * Date: 2023/10/30
// */
//
//public class SecurityUtil {
//
//    public static LoginUser getLoginUser() throws Exception {
//        try{
//            return (LoginUser) getAuthentication().getPrincipal();
//        }catch (Exception e){
//            throw new Exception("获取用户信息异常");
//        }
//    }
//
//    /**
//     * 获取Authentication
//     */
//    public static Authentication getAuthentication() {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }
//
//}