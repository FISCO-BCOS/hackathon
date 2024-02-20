//package com.find.service;
//
//import com.find.dao.AccountDao;
//import com.find.pojo.LoginUser;
//import com.find.pojo.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Description:spring-security框架之获取用户数据源，该项目没有使用前后端分离，所以该类不用理会
// * Author: Su
// * Date: 2023/10/30
// */
//
////@Service("userDetailsService")
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    AccountDao accountDao;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = accountDao.findByUsername(username);
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        return new LoginUser(user,authorities);
//    }
//}
