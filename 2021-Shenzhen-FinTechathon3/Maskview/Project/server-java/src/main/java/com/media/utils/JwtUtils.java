package com.media.utils;

import com.media.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YZR
 * @date 2020/11/7 10:18
 */

public class JwtUtils {

    @Autowired
    private UserService userService;

    /**
     * 生成token
     *
     * @param uid
     * @param phoneNumber
     * @return
     */
    public static String createToken(long uid, long phoneNumber) {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("phoneNumber", phoneNumber);
        //setExpiration:token过期时间  当前时间+有效时间
        //setClaims(claims):用户基本信息
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder()
                .setHeader(header)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRE_TIME))
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_KET);
        return builder.compact();
    }

    /**
     * 验证token是否有效
     *
     * @param token 请求头中携带的token
     * @return token验证结果  2-token过期；1-token认证通过；0-token认证失败
     */
    public static int verify(String token) {
        Claims claims;
        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            claims = Jwts.parser().setSigningKey(Constants.JWT_KET).parseClaimsJws(token).getBody();
            System.out.println(claims);
        } catch (ExpiredJwtException e) {
            return 2;
        }
        //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
        String id = claims.get("uid") + "";
        return Integer.parseInt(id);
        /*User user = iUser.selectUserById(id);
        if (user != null) {
            return 1;
        } else {
            return 0;
        }*/
    }
}
