package com.kp.cache_core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: AbstractCache <br>
 * date: 2021/9/12 4:31 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);

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

    protected abstract CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit);

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

    protected abstract CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit);

}
