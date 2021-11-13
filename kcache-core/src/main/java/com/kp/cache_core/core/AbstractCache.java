package com.kp.cache_core.core;

import com.kp.cache_core.embedded.AbstractEmbeddedCache;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.external.AbstractExternalCache;
import com.kp.cache_core.multi.MultiLevelCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.kp.cache_core.core.CacheResult.MSG_ILLEGAL_ARGUMENT;

/**
 * description: AbstractCache <br>
 * date: 2021/9/12 4:31 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);
    private final byte[] lock = new byte[0];


    @Override
    public CacheResult PUT(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        CacheResult res = null;
        if (k == null) {
            res = CacheResult.FAIL_WITH_ILLEGAL_ARGUMENT;
        } else {
            res = do_Put(k, v, expireAfterWrite, timeUnit);
        }
        return res;
    }


    @Override
    public CacheResult PUT_ALL(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        CacheResult res = null;
        if (map == null) {
            res = CacheResult.FAIL_WITH_ILLEGAL_ARGUMENT;
        } else {
            res = do_Put_All(map, expireAfterWrite, timeUnit);
        }
        return res;
    }


    @Override
    public CacheResult DELETE_ALL(List<K> keys) {
        CacheResult res = null;
        if (keys == null) {
            res = CacheResult.FAIL_WITH_ILLEGAL_ARGUMENT;
        } else {
            res = do_Delete_All(keys);
        }
        return res;
    }


    @Override
    public CacheResult DELETE(K k) {
        CacheResult res = null;
        if (k == null) {
            res = CacheResult.FAIL_WITH_ILLEGAL_ARGUMENT;
        } else {
            res = do_Delete(k);
        }
        return res;

    }

    @Override
    public CacheGetResult<V> GET(K k) {
        CacheGetResult<V> res = null;
        if (k == null) {
            res = new CacheGetResult<>(ResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
        } else {
            res = do_Get(k);
        }
        return res;

    }


    @Override
    public MultiCacheGetResult<K, V> GET_ALL(List<K> keys) {
        MultiCacheGetResult<K, V> res = null;
        if (keys == null) {
            res = new MultiCacheGetResult<>(ResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
        } else {
            res = do_Get_All(keys);
        }
        return res;
    }


    @Override
    public V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull) {
        return computeIfAbsent(key, loader, cacheNullWhenLoaderReturnNull, 0, null);
    }

    @Override
    public V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull,
                             long expireAfterWrite, TimeUnit timeUnit) {
        return computeIfAbsent(key, loader, cacheNullWhenLoaderReturnNull, 0, null, this);
    }

    private V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull,
                              long expireAfterWrite, TimeUnit timeUnit, Cache<K, V> cache) {
        CacheGetResult<V> r = cache.GET(key);

        if (r.isSuccess()) {
            return r.getData();
        } else {
            Consumer<V> consumer = (value) -> {
                if (timeUnit != null) {
                    cache.PUT(key, value, expireAfterWrite, timeUnit).waitForResult();
                } else {
                    cache.PUT(key, value).waitForResult();
                }
            };
            V value;
            if (config().isPenetrationProtect()) {
                synchronized (lock) {
                    value = loader.apply(key);
                }
            } else {
                value = loader.apply(key);
            }
            consumer.accept(value);
            return value;
        }
    }

    private static Object buildLoaderLockKey(Cache c, Object key) {
        if (c instanceof AbstractEmbeddedCache) {
            return ((AbstractEmbeddedCache) c).buildKey(key);
        } else if (c instanceof AbstractExternalCache) {
            byte bytes[] = ((AbstractExternalCache) c).buildKey(key);
            return ByteBuffer.wrap(bytes);
        } else if (c instanceof MultiLevelCache) {
            c = ((MultiLevelCache) c).getCaches()[0];
            return buildLoaderLockKey(c, key);
        } else {
            throw new CacheException("impossible");
        }
    }

    protected abstract CacheGetResult<V> do_Get(K k);

    protected abstract MultiCacheGetResult<K, V> do_Get_All(List<K> keys);

    protected abstract CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit);

    protected abstract CacheResult do_Delete_All(List<K> keys);

    protected abstract CacheResult do_Delete(K k);

    protected abstract CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit);

    @Override
    public void close() throws IOException {

    }
}
