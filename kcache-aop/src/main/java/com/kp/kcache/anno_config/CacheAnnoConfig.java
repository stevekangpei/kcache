package com.kp.kcache.anno_config;

import com.kp.cache_core.core.Cache;
import com.kp.kcache.annos.CacheType;

/**
 * description: CacheAnnoConfig <br>
 * date: 2021/11/1 7:44 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheAnnoConfig {

    /**
     * cache 实例名称
     */
    private String name;
    /**
     * cache 区域
     */
    private String area;
    /**
     * cache key
     */
    private String key;
    /**
     * cache condition
     */
    private String condition;


    private Cache<?, ?> cache;

    private CacheType cacheType;


    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Cache<?, ?> getCache() {
        return cache;
    }

    public void setCache(Cache<?, ?> cache) {
        this.cache = cache;
    }
}
