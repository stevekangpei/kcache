package com.kp.cache_core.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
    default void put(K k, V v) {
        PUT(k, v, config().getExpireAfterWriteInMillis(), TimeUnit.MILLISECONDS);
    }

    CacheConfig<K, V> config();


    default CacheResult PUT(K key, V value) {
        if (key == null) {
            return new CacheResult(ResultCode.FAIL, "Illegal Argument");
        }
        return PUT(key, value, config().getExpireAfterWriteInMillis(), TimeUnit.MILLISECONDS);
    }

    default CacheResult PUT_ALL(Map<K, V> map) {
        if (map == null) {
            return new CacheResult(ResultCode.FAIL, "Illegal Argument");
        }
        return PUT_ALL(map, config().getExpireAfterWriteInMillis(), TimeUnit.MILLISECONDS);
    }

    default void putAll(Map<K, V> map) {
        PUT_ALL(map);
    }

    default void putAll(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        PUT_ALL(map, expireAfterWrite, timeUnit);
    }


    default V computeIfAbsent(K key, Function<K, V> loader) {
        return computeIfAbsent(key, loader, config().isCacheNullValues());
    }

    V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull);

    V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull, long expireAfterWrite, TimeUnit timeUnit);

    /**
     * 内部接口的实现方法
     *
     * @param k
     * @return
     */
    CacheResult PUT(K k, V v, long expireAfterWrite, TimeUnit timeUnit);


    CacheResult PUT_ALL(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit);


    CacheResult DELETE_ALL(List<K> keys);

    CacheResult DELETE(K k);

    CacheGetResult<V> GET(K k);

    MultiCacheGetResult<K, V> GET_ALL(List<K> keys);


    @Override
    void close() throws IOException;


}
