package com.kp.kcache.aop;

import com.kp.cache_core.exception.CacheException;
import com.kp.kcache.anno_config.CacheEvictAnnoConfig;
import com.kp.kcache.anno_config.CacheUpdateAnnoConfig;
import com.kp.kcache.anno_config.CacheableAnnoConfig;
import com.kp.kcache.annos.AllowPenetration;
import com.kp.kcache.annos.CacheEvict;
import com.kp.kcache.annos.CacheUpdate;
import com.kp.kcache.annos.Cacheable;
import com.kp.kcache.aop.support.*;
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
import org.springframework.core.annotation.AnnotationUtils;
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

    @Autowired
    private CacheExecutor cacheExecutor;

    @Autowired
    private CacheBuilder cacheBuilder;

    @Around("cacheablePointCut()")
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Exception {
        Method method = getSpecificMethod(joinPoint);
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());

        try {
            String annoKey = AopUtils.getKey(method, targetClass);
            CacheableAnnoConfig cacheableConfig = annoConfigContainer.getCacheableConfigByKey(annoKey);

            if (cacheableConfig == null) {
                Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                AllowPenetration penetration = AnnotationUtils.findAnnotation(method, AllowPenetration.class);
                assert cacheable != null;
                cacheableConfig = AnnoConfigUtils.parseCacheableAnnoConfig(method, cacheable, penetration);
                annoConfigContainer.add2CacheableMap(annoKey, cacheableConfig);
            }

            CacheContext context = new CacheContext();
            context.setArgs(joinPoint.getArgs());
            context.setCacheAnnoConfig(cacheableConfig);
            context.setMethod(method);
            context.setTargetObject(joinPoint.getTarget());
            context.setInvoker(joinPoint::proceed);
            context.setCacheBuilder(cacheBuilder.buildCache());
            cacheExecutor.invokeCacheable(context);

            return context.getResult();
        } catch (Exception e) {
            logger.error("cacheablePointCut error, method {}, ", method, e);
            throw new CacheException("cacheablePointCut");
        } catch (Throwable throwable) {
            throw new CacheException("cacheablePointCut");
        }
    }


    @Around("cacheEvictPointCut()")
    public Object cacheEvict(ProceedingJoinPoint joinPoint) {
        Method method = getSpecificMethod(joinPoint);
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());

        try {
            String annoKey = AopUtils.getKey(method, targetClass);
            CacheEvictAnnoConfig evictAnnoConfig = annoConfigContainer.getCacheEvictConfigByKey(annoKey);

            if (evictAnnoConfig == null) {
                CacheEvict cacheEvict = AnnotationUtils.findAnnotation(method, CacheEvict.class);
                assert cacheEvict != null;
                evictAnnoConfig = AnnoConfigUtils.parseCacheEvictConfig(method, cacheEvict);
                annoConfigContainer.add2CacheEvictMap(annoKey, evictAnnoConfig);
            }

            CacheContext context = new CacheContext();
            context.setArgs(joinPoint.getArgs());
            context.setCacheAnnoConfig(evictAnnoConfig);
            context.setMethod(method);
            context.setTargetObject(joinPoint.getTarget());
            context.setCacheBuilder(cacheBuilder.buildCache());
            context.setInvoker(joinPoint::proceed);
            cacheExecutor.invokeEvict(context);

            return context.getResult();
        } catch (Exception e) {
            logger.error("cacheEvictPointCut error, method {}, ", method, e);
            throw new CacheException("cacheEvictPointCut");
        } catch (Throwable throwable) {
            throw new CacheException("cacheEvictPointCut");
        }
    }

    @Around("cacheUpdatePointCut()")
    public Object cacheUpdate(ProceedingJoinPoint joinPoint) {
        Method method = getSpecificMethod(joinPoint);
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());

        try {
            String annoKey = AopUtils.getKey(method, targetClass);
            CacheUpdateAnnoConfig updateAnnoConfig = annoConfigContainer.getCacheUpdateConfigByKey(annoKey);

            if (updateAnnoConfig == null) {
                CacheUpdate cacheUpdate = AnnotationUtils.findAnnotation(method, CacheUpdate.class);
                assert cacheUpdate != null;
                updateAnnoConfig = AnnoConfigUtils.parseCacheUpdateConfig(method, cacheUpdate);
                annoConfigContainer.add2CacheUpdateMap(annoKey, updateAnnoConfig);
            }

            CacheContext context = new CacheContext();
            context.setArgs(joinPoint.getArgs());
            context.setCacheAnnoConfig(updateAnnoConfig);
            context.setMethod(method);
            context.setCacheBuilder(cacheBuilder.buildCache());
            context.setTargetObject(joinPoint.getTarget());
            context.setInvoker(joinPoint::proceed);
            cacheExecutor.invokeUpdate(context);

            return context.getResult();
        } catch (Exception e) {
            logger.error("cacheUpdatePointCut error, method {}, ", method, e);
            throw new CacheException("cacheUpdatePointCut");
        } catch (Throwable throwable) {
            throw new CacheException("cacheUpdatePointCut");
        }
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
