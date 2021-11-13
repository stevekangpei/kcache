package com.kp.cache_core.multi;

import com.kp.cache_core.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * description: MultiLevelCache <br>
 * date: 2021/11/9 7:16 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class MultiLevelCache<K, V> extends AbstractCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MultiLevelCache.class);

    private MultiLevelCacheConfig config;
    private Cache[] caches;

    public MultiLevelCache(MultiLevelCacheConfig config) {
        this.config = config;
        this.caches = (Cache[]) config.getCaches().toArray(new Cache[]{});
    }


    public Cache[] getCaches() {
        return caches;
    }

    @Override
    protected CacheGetResult<V> do_Get(K k) {
        for (Cache cache : caches) {
            CacheGetResult result = cache.GET(k);
            if (result.isSuccess()) {
                CacheValueHolder holder = (CacheValueHolder) result.getData();
                return new CacheGetResult<>(ResultCode.SUCCESS, null, holder);
            }
        }
        return new CacheGetResult<>(ResultCode.NOT_EXIST, "not exist");
    }


    @Override
    protected MultiCacheGetResult<K, V> do_Get_All(List<K> keys) {
        Map<K, CacheGetResult<V>> res = new HashMap<>(keys.size());
        Set<K> kSet = new HashSet<>(keys);

        for (Cache cache : caches) {
            MultiCacheGetResult<K, V> result = cache.GET_ALL(keys);
            if (result.isSuccess() && result.getValue() != null) {
                Map<K, CacheValueHolder> value = result.getValue();

                for (Map.Entry<K, CacheValueHolder> entry : value.entrySet()) {
                    K k = entry.getKey();
                    CacheValueHolder holder = entry.getValue();
                    res.put(k, new CacheGetResult<>(ResultCode.SUCCESS, null, holder));
                    kSet.remove(k);
                }
            }
        }
        if (kSet.size() > 0) {
            logger.warn("part success for keys, we don't find value for keys, length {}", kSet.size());
        }
        return new MultiCacheGetResult<>(ResultCode.SUCCESS, "", res);
    }

    @Override
    protected CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        CompletableFuture<ResultData> future = CompletableFuture.completedFuture(null);
        for (Cache cache : caches) {
            CacheResult result = null;
            if (timeUnit == null) {
                result = cache.PUT(k, v);
            } else {
                result = cache.PUT(k, v, expireAfterWrite, timeUnit);
            }
            future(future, result);
        }
        return new CacheResult(future);
    }

    private void future(CompletableFuture<ResultData> future, CacheResult result) {
        future.thenCombine(result.getFuture(), (r1, r2) -> {
            if (r1 == null) {
                return r2;
            }
            if (r1.getResultCode() != r2.getResultCode()) {
                return new ResultData(ResultCode.PART_SUCCESS, null, null);
            }
            return r1;
        });
    }

    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        CompletableFuture<ResultData> future = CompletableFuture.completedFuture(null);
        for (Cache cache : caches) {
            CacheResult result;
            if (timeUnit == null) {
                result = cache.PUT_ALL(map);
            } else {
                result = cache.PUT_ALL(map, expireAfterWrite, timeUnit);
            }
            future(future, result);
        }
        return new CacheResult(future);
    }


    @Override
    protected CacheResult do_Delete_All(List<K> keys) {
        CompletableFuture<ResultData> future = CompletableFuture.completedFuture(null);
        for (Cache cache : caches) {
            CacheResult result = cache.DELETE_ALL(keys);
            future(future, result);
        }
        return new CacheResult(future);

    }

    @Override
    protected CacheResult do_Delete(K k) {
        CompletableFuture<ResultData> future = CompletableFuture.completedFuture(null);
        for (Cache cache : caches) {
            CacheResult result = cache.DELETE(k);
            future(future, result);
        }
        return new CacheResult(future);
    }


    @Override
    public CacheConfig<K, V> config() {
        return config;
    }
}
