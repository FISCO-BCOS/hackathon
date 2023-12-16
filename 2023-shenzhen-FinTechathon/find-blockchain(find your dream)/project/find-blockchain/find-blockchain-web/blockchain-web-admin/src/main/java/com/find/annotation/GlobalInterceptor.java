package com.find.annotation;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/24
 */

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解全局拦截器
 */
@Target({ElementType.METHOD})   // 将注解定义在方法上
@Retention(RetentionPolicy.RUNTIME) // 在执行的时候触发
@Mapping
public @interface GlobalInterceptor {

    /**
     * 校验参数
     *
     * @return
     */
    boolean checkParams() default false;

    /**
     * 校验登录
     *
     * @return
     */
    boolean checkLogin() default true;

    /**
     * 校验超级管理员
     *
     * @return
     */
    boolean checkAdmin() default false;
}
