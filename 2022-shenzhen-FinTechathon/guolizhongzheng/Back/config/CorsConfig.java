package com.zgxt.springbootdemo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author Binge
 * @desc SprintBoot 配置 CORS 跨域
 * @date 2022/9/19 17:42
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域访问的路径
                .allowedOrigins("*") //允许跨域访问的源
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE") //允许请求方法
                .allowedHeaders("*") //允许头部设置
                .maxAge(168000) //预检间隔时间
                .allowCredentials(false); // 是否发送 cookie
    }
}