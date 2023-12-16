package com.find.annotation;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/1
 */

import java.lang.annotation.*;

/**
 *延时双删
 **/
@Retention(RetentionPolicy.RUNTIME)  // 在执行的时候触发
@Documented  //指示将被注解的元素包含在生成的Java文档
@Target(ElementType.METHOD)
public @interface ClearAndReloadCache {
    String name() default "";
}