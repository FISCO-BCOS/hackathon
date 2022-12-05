package com.brecycle.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

/**
 * 用户token，在TokenFilter过滤器中使用
 *
 * @author cmgun
 */
public class UserToken implements AuthenticationToken, Serializable {

    private static final long serialVersionUID = 1841491628743017587L;

    private final String token;


    public UserToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
