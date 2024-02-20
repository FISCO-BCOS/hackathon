//package com.find.pojo;
//
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Description:
// * Author: Su
// * Date: 2023/10/30
// */
//
//@Data
//public class LoginUser implements UserDetails {
//
//    private int id;
//    private String username;
//    private String password;
//    //通过自定义方式进行授权
//    private Set<String> permissions = new HashSet<String>();
//
//    //通过springSecurity进行授权
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public LoginUser(){}
//
//    public LoginUser(User user, Collection<? extends GrantedAuthority> authorities) {
//        id = user.getId();
//        username = user.getUsername();
//        password = user.getPassword();
//        System.out.println("user.getPermission():" + user.getPermission());
//        permissions.add(user.getPermission());
//        System.out.println("permissions:" + permissions);
//        this.authorities = authorities;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
