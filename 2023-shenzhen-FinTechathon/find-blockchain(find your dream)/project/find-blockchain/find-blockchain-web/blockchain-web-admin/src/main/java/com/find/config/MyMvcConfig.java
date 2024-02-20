package com.find.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
    }

    //重载登录拦截组件，设置拦截范围
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**", "/index.html", "/css/**", "/img/**",
                        "/layui/**", "/", "/index","/ipfs/**","/fl/querySecurityNode",
                        "/fl/deleteNode","/webSocket_node/*","/webSocket/*",
                        "/fl/requestTask","/res/getTrainDataByUser","/res/test");
    }

}
