package com.kp.cache_core.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JSR_107 API Style
 * 1, GET/GEL_ALL (对外部暴露的接口为get/getAll)
 * 2, PUT/PUL_ALL
 * 3, DELETE/DELETE_ALL
 * 4, computeIfAbsent
 * 5,
 * description: Cache <br>
 * date: 2021/7/25 10:05 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface Cache<K, V> extends Closeable {



    /**
     * 外部使用这个接口方法来和缓存交互
     *
     * @param k
     */
    default void put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        PUT(k, v, expireAfterWrite, timeUnit);
    }

    /**
     * 内部接口的实现方法
     *
     * @param k
     * @return
     */
    CacheResult PUT(K k, V v, long expireAfterWrite, TimeUnit timeUnit);

    default void putAll(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        PUT_ALL(map, expireAfterWrite, timeUnit);
    }

    CacheResult PUT_ALL(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit);

    @Override
    void close() throws IOException;


}
