package com.kp.kcache.aop.support;

import com.kp.cache_core.core.Cache;
import com.kp.kcache.anno_config.CacheAnnoConfig;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * description: CacheContext <br>
 * date: 2021/11/2 7:41 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheContext {

    private Invoker invoker;

    private Method method;

    private Object[] args;

    private Object result;

    private Function<CacheAnnoConfig, Cache> cacheBuilder;

    private CacheAnnoConfig cacheAnnoConfig;

    private Object targetObject;

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Function<CacheAnnoConfig, Cache> getCacheBuilder() {
        return cacheBuilder;
    }

    public void setCacheBuilder(Function<CacheAnnoConfig, Cache> cacheBuilder) {
        this.cacheBuilder = cacheBuilder;
    }

    public CacheAnnoConfig getCacheAnnoConfig() {
        return cacheAnnoConfig;
    }

    public void setCacheAnnoConfig(CacheAnnoConfig cacheAnnoConfig) {
        this.cacheAnnoConfig = cacheAnnoConfig;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }
}
