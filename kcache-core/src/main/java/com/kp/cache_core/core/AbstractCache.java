package com.kp.cache_core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.kp.cache_core.core.CacheResult.MSG_ILLEGAL_ARGUMENT;

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
