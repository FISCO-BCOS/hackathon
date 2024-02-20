package com.find.annotation;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/24
 */

import com.find.enums.VerifyRegexEnum;

import java.lang.annotation.*;

/**
 * 校验参数的注解定义
 */
@Target({ElementType.PARAMETER, ElementType.FIELD}) // 可以定义在字段和形参上
@Retention(RetentionPolicy.RUNTIME) // 启动的时候生效
@Documented
public @interface VerifyParam {
    /**
     * 是否校验
     * 必填项
     *
     * @return
     */
    boolean required() default false;

    /**
     * 校验值不为空
     *
     * @return
     */
    boolean NotBlank() default false;

    /**
     * 校验值不为空
     *
     * @return
     */
    boolean NotNull() default false;

    /**
     * 校验最小长度
     *
     * @return
     */
    int min() default -1;

    /**
     * 校验最大长度
     *
     * @return
     */
    int max() default -1;

    /**
     * 校验最小值
     *
     * @return
     */
    int minValue() default -1;

    /**
     * 校验正则
     *
     * @return
     */
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;

    /**
     * 设置分组
     *
     * @return
     */
    String[] group() default {};
}
