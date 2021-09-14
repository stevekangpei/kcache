package com.kp.cache_core.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
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
     * JSR-----107 Java code style
     */


    default V get(K k) {
        CacheGetResult<V> v = GET(k);
        if (v.isSuccess()) {
            return v.getData();
        }
        return null;
    }

    default Map<K, V> getAll(List<K> keys) {
        MultiCacheGetResult<K, V> result = GET_ALL(keys);
        if (result.isSuccess()) {
            return result.unWrapValues();
        }
        return null;
    }

    default boolean delete(K k) {
        return DELETE(k).isSuccess();
    }

    default boolean deleteAll(List<K> keys) {
        return DELETE_ALL(keys).isSuccess();
    }


    /**
     * 外部使用这个接口方法来和缓存交互
     *
     * @param k
     */
    default void put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        PUT(k, v, expireAfterWrite, timeUnit);
    }

    CacheConfig<K, V> config();

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


    CacheResult DELETE_ALL(List<K> keys);

    CacheResult DELETE(K k);

    CacheGetResult<V> GET(K k);

    MultiCacheGetResult<K, V> GET_ALL(List<K> keys);


    @Override
    void close() throws IOException;


}
