package com.kp.cache_core.cache_builder;

import com.kp.cache_core.core.Cache;
import com.kp.cache_core.core.CacheConfig;

import java.util.function.Function;

/**
 * description: AbstractCacheBuilder <br>
 * date: 2021/9/15 7:57 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheBuilder implements ICacheBuilder {

    protected CacheConfig config;
    protected Function<CacheConfig, Cache> buildFunc;

    protected void beforeBuild() {

    }

    protected void afterBuild() {
    }



    @Override
    public final <K, V> Cache<K, V> buildCache(CacheConfig<K, V> config) {
        beforeBuild();
        /**
         * 由于这个地方获取的是全局配置，防止后序修改。在这个地方做一次深拷贝。
         */
        CacheConfig configClone = (CacheConfig) config.clone();
        Cache<K, V> cache = (Cache<K, V>) buildFunc.apply(config);
        afterBuild();
        return cache;
    }


    public CacheConfig getConfig() {
        return config;
    }

    public void setConfig(CacheConfig config) {
        this.config = config;
    }

    public Function<CacheConfig, Cache> getBuildFunc() {
        return buildFunc;
    }

    public void setBuildFunc(Function<CacheConfig, Cache> buildFunc) {
        this.buildFunc = buildFunc;
    }

}
