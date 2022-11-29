package com.brecycle.config.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 解析和生成 token
 *
 * @author cmgun
 */
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -5625635588908941275L;


    public static String generateToken(String username, Long current) {
        if (null == current) {
            current = System.currentTimeMillis();
        }
        Algorithm algorithm = Algorithm.HMAC256(JWTConfig.secret);
        return JWT.create()
                .withClaim(JWTConfig.authoritiesKey, username)
                .withClaim("current", current)
                .withExpiresAt(new Date(current + JWTConfig.expiration * 1000))
                .sign(algorithm);
    }

    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(JWTConfig.authoritiesKey).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getToken() {
        String token;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        token = request.getHeader(JWTConfig.tokenHeader);
        if (StringUtils.isBlank(token)) {
            return "";
        }
        return token;
    }

    public static boolean isExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expiration = jwt.getExpiresAt();
        return expiration.before(new Date());
    }

    public static boolean verify(String token) {
        try {
            //解密
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWTConfig.secret)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getCurrent(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("current").asLong();
        }catch (Exception e){
            return null;
        }
    }
}
