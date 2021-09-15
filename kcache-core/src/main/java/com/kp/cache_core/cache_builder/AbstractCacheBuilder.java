package com.kp.cache_core.cache_builder;

import com.kp.cache_core.core.Cache;
import com.kp.cache_core.core.CacheConfig;
import org.checkerframework.checker.units.qual.K;

import java.util.function.Function;

/**
 * description: AbstractCacheBuilder <br>
 * date: 2021/9/15 7:57 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheBuilder implements ICacheBuilder {

    protected CacheConfig<K, V> config;
    protected Function<CacheConfig<K, V>, Cache<K, V>> buildFunc;

    protected void beforeBuild() {

    }

    @Override
    public final <K, V> Cache<K, V> buildCache(CacheConfig<K, V> config) {
        beforeBuild();
        Cache<K, V> cache = (Cache<K, V>) buildFunc.apply(config);
        return cache;
    }


    public CacheConfig<K, V> getConfig() {
        return config;
    }

    public void setConfig(CacheConfig<K, V> config) {
        this.config = config;
    }

    public Function<CacheConfig<K, V>, Cache<K, V>> getBuildFunc() {
        return buildFunc;
    }

    public void setBuildFunc(Function<CacheConfig<K, V>, Cache<K, V>> buildFunc) {
        this.buildFunc = buildFunc;
    }

}
