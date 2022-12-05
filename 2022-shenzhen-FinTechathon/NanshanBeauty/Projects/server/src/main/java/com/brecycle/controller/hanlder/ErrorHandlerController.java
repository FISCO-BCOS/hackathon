package com.brecycle.controller.hanlder;

import com.brecycle.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 *
 * @author cmgun
 */
@Slf4j
@ControllerAdvice
public class ErrorHandlerController {

    /**
     * 登录异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public Response loginExceptionHandler(HttpServletRequest request, HttpServletResponse response, LoginException exception) {
//        response.setStatus(Response.UNAUTHORIZED.intValue());
        log.warn("LoginException，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.UNAUTHORIZED)
                .msg(exception.getMessage())
                .build();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Response businessExceptionHandler(HttpServletRequest request, HttpServletResponse response, BusinessException exception) {
//        response.setStatus(Response.UNAUTHORIZED.intValue());
        log.warn("BusinessException，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.ERROR)
                .msg(exception.getMessage())
                .build();
    }

    /**
     * 其他异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        log.warn("ExceptionHandler，url:{}", request.getRequestURI(), exception);
//        response.setStatus(Response.ERROR.intValue());
        return Response.builder()
                .code(Response.ERROR)
                .msg("系统内部异常")
                .build();
    }
}
