package com.brecycle.config.shiro;

import com.alibaba.fastjson.JSON;
import com.brecycle.common.Response;
import com.brecycle.config.redis.RedisConstant;
import com.brecycle.config.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token过滤器，判断用户是否已经登录
 *
 * @author cmgun
 */
@Slf4j
public class TokenFilter extends BasicHttpAuthenticationFilter {

    /**
     * 如果带有 com.token，则对 com.token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 com.token 是否正确
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                log.error("shiro验证异常 {}", Arrays.asList(e.getStackTrace()));
                responseError(response, "shiro fail");
                return false;
            }
        }
        //1.如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 com.token，直接返回 true
        //2.如果请求头不存在 Token，并且返回false，说明走我们的过滤器时必须带token
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 JWTConfig.tokenHeader 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            String token = req.getHeader(JWTConfig.tokenHeader);
            return StringUtils.isNotBlank(token);
        } catch (Exception e) {
            log.info("获取token失败");
        }
        return false;
    }

    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(JWTConfig.tokenHeader);
        UserToken userToken = new UserToken(token);
        try {
            Subject subject = this.getSubject(request, response);
            subject.login(userToken);
            return this.onLoginSuccess(userToken, subject, request, response);
        } catch (AuthenticationException var5) {
            return this.onLoginFailure(userToken, var5, request, response);
        }
    }

//    /**
//     * 对跨域提供支持
//     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }

    /**
     * token错误
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        try {
            String token = ((HttpServletRequest) request).getHeader(JWTConfig.tokenHeader);
            log.info("--------token过期:{}--------", token);
        } catch (Exception e) {
            log.info("--------token不存在--------");
        }
        this.sendChallenge(request, response);
        this.responseError(response, "token已过期或不存在!");
        return Boolean.FALSE;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        log.info("--------onLoginSuccess--------");
        String tokenPrincipal = (String) token.getPrincipal();
        if (StringUtils.isNotBlank(tokenPrincipal)) {
            if (JwtTokenUtil.verify(tokenPrincipal)) {
                return true;
            } else {
                return refreshToken(request, response);
            }
        }
        return true;
    }

    /**
     * 非法请求处理
     */
    private void responseError(ServletResponse response, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        response.reset();
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        Response error = Response.builder().code(Response.UNAUTHORIZED).msg("无请求权限").build();

        try (PrintWriter out = response.getWriter()) {
            out.print(JSON.toJSONString(error));
        } catch (IOException e) {
            log.error("sendChallenge error,can not resolve httpServletResponse");
        }

    }

    public <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }

    /**
     * 刷新token
     *
     * @param request
     * @param response
     * @return
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        RedisUtil redisUtil = getBean(RedisUtil.class, req);
        // 获取传递过来的accessToken
        String accessToken = req.getHeader(JWTConfig.tokenHeader);
        // 获取token里面的用户名
        String username = JwtTokenUtil.getUsername(accessToken);
        // 判断refreshToken是否过期了，过期了那么所含的username的键不存在
        if (redisUtil.hasKey(RedisConstant.CACHE_PREFIX + RedisConstant.USER_TOKEN_KEY + username)) {
            // 判断refresh的时间节点和传递过来的accessToken的时间节点是否一致，不一致校验失败
            Long current = (Long) redisUtil.getCacheObject(RedisConstant.CACHE_PREFIX + RedisConstant.USER_TOKEN_KEY + username);
            if (Objects.equals(JwtTokenUtil.getCurrent(accessToken), current)) {
                // 获取当前时间节点
                long currentTimeMillis = System.currentTimeMillis();
                // 生成刷新的token
                String token = JwtTokenUtil.generateToken(username, currentTimeMillis);
                // 刷新redis里面的refreshToken,过期时间是(JWTConfig.expiration + 30*60)min
                redisUtil.setCacheObject(RedisConstant.CACHE_PREFIX + RedisConstant.USER_TOKEN_KEY + username, currentTimeMillis
                        , JWTConfig.expiration + JWTConfig.redisExpiration, TimeUnit.SECONDS);
                // 最后将刷新的AccessToken存放在Response的Header中的JWTConfig.tokenHeader字段返回
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader(JWTConfig.tokenHeader, token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", JWTConfig.tokenHeader);
                return true;
            }
        }
        return false;
    }
}