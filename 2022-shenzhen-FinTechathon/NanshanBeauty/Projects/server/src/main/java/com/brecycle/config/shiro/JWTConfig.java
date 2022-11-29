package com.brecycle.config.shiro;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt配置
 *
 * @author cmgun
 */
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {

    public static String authoritiesKey;

    /**
     * 密钥KEY
     */
    public static String secret;

    /**
     * TokenKey
     */
    public static String tokenHeader;
    /**
     * 过期时间
     */
    public static Integer expiration;
    /**
     * redis过期时间
     */
    public static Integer redisExpiration;

    public void setAuthoritiesKey(String authoritiesKey) {
        JWTConfig.authoritiesKey = authoritiesKey;
    }

    public void setSecret(String secret) {
        JWTConfig.secret = secret;
    }

    public void setTokenHeader(String tokenHeader) {
        JWTConfig.tokenHeader = tokenHeader;
    }

    public void setExpiration(Integer expiration) {
        JWTConfig.expiration = expiration;
    }

    public void setRedisExpiration(Integer redisExpiration) {
        JWTConfig.redisExpiration = redisExpiration;
    }
}
