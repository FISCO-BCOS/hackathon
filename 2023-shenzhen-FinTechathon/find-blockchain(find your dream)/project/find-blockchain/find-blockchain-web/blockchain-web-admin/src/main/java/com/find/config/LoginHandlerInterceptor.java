package com.find.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.find.controller.FlController.sessionId;


public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if ("/user/login".equals(requestURI)) {
            return true;
        } else {
            HttpSession session = request.getSession();
//            System.out.println(requestURI + ":" + session.getId());
//            System.out.println(request.getHeader("Cookie"));
//            System.out.println(session.getAttribute("login"));
//            System.out.println(session.getId());
//            if (request.getHeader("Cookie").equals(sessionId) || session.getAttribute("login") != null){
            if (session.getAttribute("login") != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
