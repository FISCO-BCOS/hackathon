package com.find.aspect;

import com.find.annotation.GlobalInterceptor;
import com.find.annotation.VerifyParam;
import com.find.exception.ValidationException;
import com.find.util.StringTools;
import com.find.util.VerifyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/30
 */

@Aspect // 切面
@Component("GlobalOperationAspect")
public class GlobalOperationAspect {
    private static final String[] TYPES = {"java.lang.String", "java.lang.Integer", "java.lang.Long"};

    // 定义一个切点，以注解为切点
    @Pointcut("@annotation(com.find.annotation.GlobalInterceptor)")
    private void requestInterceptor() {
    }

    @Before("requestInterceptor()")
    private void interceptorDo(JoinPoint point) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException {

        // 获取注解(代理目标)对象
        Object target = point.getTarget();
        // 获取参数列表
        Object[] arguments = point.getArgs();
        // 获取方法对象的名称
        String methodName = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        //反射获取方法对象
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);

        if (null == interceptor) {
            return;
        }

        /**
         * 校验登录
         */
        if (interceptor.checkLogin() || interceptor.checkAdmin()) {
            checkLogin(interceptor.checkAdmin());
        }

        /**
         * 校验参数
         */
        if (interceptor.checkParams()) {
            validateParams(method, arguments);
        }
    }

    /**
     * 校验登录
     *
     * @param checkAdmin
     */
    public void checkLogin(boolean checkAdmin) {

    }

    /**
     * 参数校验
     *
     * @param m         方法对象
     * @param arguments 参数集合
     */
    private void validateParams(Method m, Object[] arguments) throws ClassNotFoundException, IllegalAccessException {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if (verifyParam == null) {
                continue;
            }
            // 基本数据类型
            if (Arrays.asList(TYPES).contains(parameter.getParameterizedType().getTypeName())) {
                checkValue(value, verifyParam, new String[]{});
                // 如果传递的是对象
            } else {
                checkObjValue(parameter, value);
            }
        }
    }

    /**
     * 校验对象
     * 将对象中的属性一个一个判断
     *
     * @param parameter
     * @param value     对象值
     */
    private void checkObjValue(Parameter parameter, Object value) throws ClassNotFoundException, IllegalAccessException {
        String typeName = parameter.getParameterizedType().getTypeName();
        Class classz = Class.forName(typeName);
        Field[] fields = classz.getDeclaredFields();
        //遍历对象中的字段，获取字段的注解
        String[] group = {};
        VerifyParam fieldVerifyParam = parameter.getAnnotation(VerifyParam.class);
        if (fieldVerifyParam == null) {
            return;
        }
        if(fieldVerifyParam.group().length != 0){
            System.out.println("group++++");
            group = fieldVerifyParam.group();
        }
        for (Field field : fields) {
            //获取该字段的注解信息，判断是否有添加注解
            VerifyParam param = field.getAnnotation(VerifyParam.class);
            if(param != null){
                System.out.println("字段的注解：" + field + param.toString());
                //设置该字段可访问
                field.setAccessible(true);
                Object resultValue = field.get(value);
//                Object resultKey = field.getName();
                checkValue(resultValue, param, group);
            }
        }
    }

    /**
     * 校验参数
     *
     * @param value       待校验的参数
     * @param verifyParam 注解对象
     */
    private void checkValue(Object value, VerifyParam verifyParam,String[] group) {
//        System.out.println("++++++++++" + (Arrays.toString(verifyParam.group())) + "--" + group);
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();

        /**
         * 校验null
         */
        if (isEmpty && verifyParam.required() && verifyParam.NotNull() && (group.length == 0 || verifyParam.group().length == 0)) {
            throw new ValidationException("数值不能为null");
        }else if(group.length > 0){   //该字段设置了分组
            System.out.println("group长度：" + verifyParam.group().length);
            System.out.println("进入name分组判断");
            for (String s : group) {
                System.out.println(Arrays.toString(verifyParam.group()) + "----" + s);
                if(Arrays.asList(verifyParam.group()).contains(s)){
                    System.out.println("成功");
                    if(isEmpty && verifyParam.required() && verifyParam.NotNull()){
                        throw new ValidationException("数值不能为null");
                    }
                }
            }
        }


        /**
         * 校验空值
         */
        if (isEmpty && verifyParam.required() && verifyParam.NotBlank() && (group.length == 0 || verifyParam.group().length == 0)) {
            throw new ValidationException("输入的数值不能为空");
        }else if(group.length > 0){   //该字段设置了分组
            System.out.println("group长度：" + verifyParam.group().length);
            System.out.println("进入name分组判断");
            for (String s : group) {
                System.out.println(Arrays.toString(verifyParam.group()) + "----" + s);
                if(Arrays.asList(verifyParam.group()).contains(s)){
                    System.out.println("成功");
                    if(isEmpty && verifyParam.required() && verifyParam.NotBlank()){
                        throw new ValidationException("数值不能为空");
                    }
                }
            }
        }

        /**
         * 校验长度
         */
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1
                && verifyParam.min() > length) && (group.length == 0 || verifyParam.group().length == 0)) {

            System.out.println("校验长度");
            throw new ValidationException("输入的字符:"+ value +"长度不符合要求");
        }else if(group.length > 0){  //该字段设置了分组
            System.out.println("group长度：" + verifyParam.group().length);
            System.out.println("进入name分组判断");
            for (String s : group) {
                System.out.println(Arrays.toString(verifyParam.group()) + "----" + s);
                if(Arrays.asList(verifyParam.group()).contains(s)){
                    System.out.println("成功");
                    if(!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length)){
                        throw new ValidationException("输入的字符:"+ value +"长度不符合要求");
                    }
                }
            }
        }

        /**
         * 校验最小值
         */
        if (!isEmpty && (verifyParam.minValue() != -1) && (int)value < verifyParam.minValue() && (group.length == 0 || verifyParam.group().length == 0)) {
            System.out.println("校验最小值");
            throw new ValidationException("输入的数值:"+ value +"小于限制的最小值");
        }else if(group.length > 0){
            System.out.println("group长度：" + verifyParam.group().length);
            System.out.println("进入name分组判断");
            for (String s : group) {
                System.out.println(Arrays.toString(verifyParam.group()) + "----" + s);
                if(Arrays.asList(verifyParam.group()).contains(s)){
                    System.out.println("成功");
                    if(!isEmpty && (verifyParam.minValue() != -1) && (int)value < verifyParam.minValue() ){
                        throw new ValidationException("输入的数值:"+ value +"小于限制的最小值");
                    }
                }
            }
        }

        /**
         * 校验正则
         */
        if (!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex().getRegex(),
                String.valueOf(value)) && (group.length == 0 || verifyParam.group().length == 0)) {

            System.out.println("校验正则值");
            throw new ValidationException("输入的数据:"+ value +"格式有误");
        }else if(group.length > 0){
            System.out.println("group长度：" + verifyParam.group().length);
            System.out.println("进入正则分组判断");
            for (String s : group) {
                System.out.println(Arrays.toString(verifyParam.group()) + "----" + s);
                if(Arrays.asList(verifyParam.group()).contains(s)){
                    if(!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex().getRegex() ,String.valueOf(value))){
                        throw new ValidationException("输入的数据:"+ value +"格式有误");
                    }
                }
            }
        }
    }
}
