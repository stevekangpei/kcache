package com.kp.kcache.aop.support;

import com.kp.kcache.anno_config.CacheEvictAnnoConfig;
import com.kp.kcache.anno_config.CacheUpdateAnnoConfig;
import com.kp.kcache.anno_config.CacheableAnnoConfig;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: AnnoConfigContainer <br>
 * date: 2021/11/1 10:35 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
public class AnnoConfigContainer {

    private Map<String, CacheableAnnoConfig> cacheableAnnoConfigMap = new ConcurrentHashMap<>(256);

    private Map<String, CacheEvictAnnoConfig> cacheEvictAnnoConfigMap = new ConcurrentHashMap<>(256);

    private Map<String, CacheUpdateAnnoConfig> cacheUpdateAnnoConfigMap = new ConcurrentHashMap<>(256);

    public void add2CacheableMap(String key, CacheableAnnoConfig cacheableAnnoConfig) {
        this.cacheableAnnoConfigMap.put(key, cacheableAnnoConfig);
    }

    public CacheableAnnoConfig getCacheableConfigByKey(String key) {
        return this.cacheableAnnoConfigMap.get(key);
    }

    public void add2CacheEvictMap(String key, CacheEvictAnnoConfig cacheEvictAnnoConfig) {
        this.cacheEvictAnnoConfigMap.put(key, cacheEvictAnnoConfig);
    }

    public CacheEvictAnnoConfig getCacheEvictConfigByKey(String key) {
        return this.cacheEvictAnnoConfigMap.get(key);
    }

    public void add2CacheUpdateMap(String key, CacheUpdateAnnoConfig cacheUpdateAnnoConfig) {
        this.cacheUpdateAnnoConfigMap.put(key, cacheUpdateAnnoConfig);
    }

    public CacheUpdateAnnoConfig getCacheUpdateConfigByKey(String key) {
        return this.cacheUpdateAnnoConfigMap.get(key);
    }


}
