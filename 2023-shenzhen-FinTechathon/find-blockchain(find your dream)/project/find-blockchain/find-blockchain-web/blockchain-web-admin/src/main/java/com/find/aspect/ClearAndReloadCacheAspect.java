package com.find.aspect;

import com.find.annotation.ClearAndReloadCache;
import com.find.service.FlService;
import com.find.util.Constant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/1
 */

@Aspect
@Component
public class ClearAndReloadCacheAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FlService flService;

    /**
     * 切入点
     *切入点,基于注解实现的切入点  加上该注解的都是Aop切面的切入点
     *
     */

    @Pointcut("@annotation(com.find.annotation.ClearAndReloadCache)")
    public void pointCut(){

    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     * @param proceedingJoinPoint
     */
    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("----------- 环绕通知 -----------");
        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());

        Signature signature1 = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature1;
        //Spring AOP 提供的一种简便方式去获取方法对象，本质也是通过反射来获取方法对象
        Method targetMethod = methodSignature.getMethod();
        ClearAndReloadCache annotation = targetMethod.getAnnotation(ClearAndReloadCache.class);//反射得到自定义注解的方法对象

        stringRedisTemplate.delete(Constant.SECURITY_LIST);//模糊删除redis的key值
        System.out.println("第一次删除");
        //执行加入双删注解的改动数据库的业务 即controller中的方法业务
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        // 在线程中延迟删除  同时将业务代码的结果返回 这样不影响业务代码的执行
        new Thread(() -> {
            try {
                //延时500ms删除
                Thread.sleep(500);
//                Set<String> keys1 = stringRedisTemplate.keys("*" + name + "*");//模糊删除
                stringRedisTemplate.delete(Constant.SECURITY_LIST);
                System.out.println("-----------10秒钟后，在线程中延迟第二次删除完毕 -----------");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return proceed;//返回业务代码的值
    }
}
