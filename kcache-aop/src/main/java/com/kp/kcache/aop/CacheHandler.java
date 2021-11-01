package com.kp.kcache.aop;

import com.kp.kcache.aop.support.AnnoConfigContainer;
import com.kp.kcache.aop.support.AopUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * description: CacheHandler <br>
 * date: 2021/11/1 7:52 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Aspect
public class CacheHandler {
    private static final Logger logger = LoggerFactory.getLogger(CacheHandler.class);

    @Pointcut("@annotation(com.kp.kcache.annos.Cacheable)")
    public void cacheablePointCut() {
    }

    @Pointcut("@annotation(com.kp.kcache.annos.CacheUpdate)")
    public void cacheUpdatePointCut() {
    }

    @Pointcut("@annotation(com.kp.kcache.annos.CacheEvict)")
    public void cacheEvictPointCut() {
    }

    @Autowired
    private AnnoConfigContainer annoConfigContainer;

    @Around("cacheablePointCut()")
    public Object cacheable(ProceedingJoinPoint joinPoint) {
        Method method = getSpecificMethod(joinPoint);
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        String annoKey = AopUtils.getKey(method, targetClass);

    }


    @Around("cacheEvictPointCut()")
    public Object cacheEvict(ProceedingJoinPoint joinPoint) {

    }

    @Around("cacheUpdatePointCut()")
    public Object cacheUpdate(ProceedingJoinPoint joinPoint) {

    }

    private Method getSpecificMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }


}
