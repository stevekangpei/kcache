package com.kp.kcache.aop.support;

import com.kp.cache_core.core.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: CacheContainer <br>
 * date: 2021/11/3 9:59 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheContainer {

    private static final Map<String, Map<String, Cache>> container = new ConcurrentHashMap<>(256);

    public static Cache getCacheByAreaAndName(String area, String cacheName) {
        Map<String, Cache> map = container.computeIfAbsent(area, k -> new ConcurrentHashMap<>());
        return map.get(cacheName);
    }

    public static void addCache(String area, String cacheName, Cache cache) {
        Map<String, Cache> map = container.computeIfAbsent(area, k -> new ConcurrentHashMap<>());
        map.put(cacheName, cache);
    }
}
