package com.kp.kcache.autoconfigure.support;

import com.kp.cache_core.cache_builder.embedded.EmbeddedCacheBuilder;
import com.kp.cache_core.cache_builder.external.ExternalCacheBuilder;

import java.util.Map;

/**
 * description: GlobalCacheConfig <br>
 * date: 2021/10/16 10:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class GlobalCacheConfig {

    /**
     * 是否开启保护模式（缓存未命中，同一个JVM里面是否只允许一个线程去执行原方法，其它线程等待结果）
     */
    private boolean penetrationProtect = false;
    /**
     * 是否开启缓存
     */
    private boolean enableMethodCache = true;
    /**
     * 保存本地缓存 CacheBuilder 构造器
     */
    private Map<String, EmbeddedCacheBuilder> localCacheBuilders;
    /**
     * 保存远程缓存 CacheBuilder 构造器
     */
    private Map<String, ExternalCacheBuilder> remoteCacheBuilders;


    public boolean isPenetrationProtect() {
        return penetrationProtect;
    }

    public void setPenetrationProtect(boolean penetrationProtect) {
        this.penetrationProtect = penetrationProtect;
    }

    public boolean isEnableMethodCache() {
        return enableMethodCache;
    }

    public void setEnableMethodCache(boolean enableMethodCache) {
        this.enableMethodCache = enableMethodCache;
    }

    public Map<String, EmbeddedCacheBuilder> getLocalCacheBuilders() {
        return localCacheBuilders;
    }

    public void setLocalCacheBuilders(Map<String, EmbeddedCacheBuilder> localCacheBuilders) {
        this.localCacheBuilders = localCacheBuilders;
    }

    public Map<String, ExternalCacheBuilder> getRemoteCacheBuilders() {
        return remoteCacheBuilders;
    }

    public void setRemoteCacheBuilders(Map<String, ExternalCacheBuilder> remoteCacheBuilders) {
        this.remoteCacheBuilders = remoteCacheBuilders;
    }
}
