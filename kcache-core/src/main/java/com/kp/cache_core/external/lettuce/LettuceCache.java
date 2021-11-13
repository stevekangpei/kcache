package com.kp.cache_core.external.lettuce;

import com.kp.cache_core.core.*;
import com.kp.cache_core.external.AbstractExternalCache;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * description: LettuceCache <br>
 * date: 2021/9/12 6:19 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCache<K, V> extends AbstractExternalCache<K, V> {

    private LettuceCacheConfig<K, V> config;

    private Function<Object, byte[]> encoder;
    private Function<byte[], Object> decoder;
    private final AbstractRedisClient client;
    /**
     * 异步命令
     */
    private RedisStringAsyncCommands<byte[], byte[]> stringAsyncCommands;
    /**
     * 响应式命令
     */
    private RedisKeyAsyncCommands<byte[], byte[]> keyAsyncCommands;


    public LettuceCache(LettuceCacheConfig<K, V> config) {
        super(config);
        this.config = config;
        this.encoder = config.getEncoder();
        this.decoder = config.getDecoder();
        this.client = config.getClient();

    }

    @Override
    protected CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            CacheValueHolder holder = new CacheValueHolder(v, timeUnit.toMillis(expireAfterWrite));
            byte[] key = buildKey(k);
            RedisFuture<String> future = stringAsyncCommands.psetex(key, timeUnit.toMillis(expireAfterWrite), encoder.apply(holder));

            return new CacheResult(future.handle((res, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                } else {
                    if ("OK".equalsIgnoreCase(res)) {
                        return new ResultData(ResultCode.SUCCESS, "", null);
                    } else {
                        return new ResultData(ResultCode.FAIL, "", null);
                    }
                }
            }));
        } catch (Exception e) {
            return new CacheResult(e);
        }
    }


    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            CompletionStage<Integer> future = CompletableFuture.completedFuture(0);
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                CacheValueHolder holder = new CacheValueHolder(value, timeUnit.toMillis(expireAfterWrite));
                RedisFuture<String> resp = stringAsyncCommands.psetex(buildKey(key), timeUnit.toMillis(expireAfterWrite), encoder.apply(holder));
                future.thenCombine(resp, (fails, str) -> "Ok".equalsIgnoreCase(str) ? fails : fails + 1);
            }
            return new CacheResult(future.handle((res, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                } else if (res == 0) {
                    return new ResultData(ResultCode.SUCCESS, null, null);
                } else if (res == map.size()) {
                    return new ResultData(ResultCode.FAIL, null, null);
                } else {
                    return new ResultData(ResultCode.PART_SUCCESS, null, null);
                }
            }));
        } catch (Exception e) {
            return CacheResult.FAIL_WITHOUT_MSG;
        }
    }

    @Override
    protected CacheGetResult<V> do_Get(K k) {
        try {
            byte[] bytes = buildKey(k);
            RedisFuture<byte[]> future = stringAsyncCommands.get(bytes);

            if (future == null) {
                return new CacheGetResult<>(ResultCode.NOT_EXIST, "not exist", null);
            } else {
                CacheValueHolder holder = (CacheValueHolder) decoder.apply(future.get());
                long now = System.currentTimeMillis();
                if (now > holder.getExpireTime()) {
                    return new CacheGetResult<>(ResultCode.EXPIRED, "expired", null);
                }
                return new CacheGetResult<>(ResultCode.SUCCESS, "success", holder);
            }
        } catch (Exception e) {
            return new CacheGetResult<V>(e);
        }
    }

    @Override
    protected MultiCacheGetResult<K, V> do_Get_All(List<K> keys) {
        try {
            byte[][] bytes = keys.stream().map(this::buildKey).toArray((len) -> new byte[keys.size()][]);
            RedisFuture<List<KeyValue<byte[], byte[]>>> resp = stringAsyncCommands.mget(bytes);
            Map<K, CacheGetResult<V>> resultMap = new HashMap<>();

            MultiCacheGetResult<K, V> result = new MultiCacheGetResult<>(resp.handle((res, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                } else {
                    long now = System.currentTimeMillis();
                    for (int i = 0; i < res.size(); i++) {
                        KeyValue<byte[], byte[]> keyValue = res.get(i);
                        K k = keys.get(i);
                        CacheValueHolder holder = (CacheValueHolder) decoder.apply(keyValue.getValue());
                        if (now > holder.getExpireTime()) {
                            resultMap.put(k, new CacheGetResult<>(ResultCode.EXPIRED, null));
                        } else {
                            resultMap.put(k, new CacheGetResult<>(ResultCode.SUCCESS, "success", holder));
                        }
                    }
                }
                return new ResultData(ResultCode.SUCCESS, "", resultMap);
            }));
            return result;
        } catch (Exception e) {
            return new MultiCacheGetResult<>(e);
        }
    }

    @Override
    protected CacheResult do_Delete_All(List<K> keys) {
        try {
            byte[][] bytes = keys.stream().map(this::buildKey).toArray((len) -> new byte[keys.size()][]);
            RedisFuture<Long> del = keyAsyncCommands.del(bytes);
            CacheResult cacheResult = new CacheResult(del.handle((res, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                }
                return new ResultData(ResultCode.SUCCESS, null, null);
            }));
            return cacheResult;
        } catch (Exception e) {
            return CacheResult.FAIL_WITHOUT_MSG;
        }
    }

    @Override
    protected CacheResult do_Delete(K k) {
        try {
            RedisFuture<Long> del = keyAsyncCommands.del(buildKey(k));
            CacheResult cacheResult = new CacheResult(del.handle((res, ex) -> {
                if (ex != null) {
                    return new ResultData(ex);
                } else {
                    if (res == null) {
                        return new ResultData(ResultCode.FAIL, null, null);
                    } else if (res == 1) {
                        return new ResultData(ResultCode.SUCCESS, null, null);
                    } else if (res == 0) {
                        return new ResultData(ResultCode.NOT_EXIST, null, null);
                    } else {
                        return new ResultData(ResultCode.FAIL, null, null);
                    }
                }
            }));
            return cacheResult;
        } catch (Exception e) {
            return CacheResult.FAIL_WITHOUT_MSG;
        }
    }

    @Override
    public CacheConfig<K, V> config() {
        return this.config;
    }

    @Override
    public void close() throws IOException {

    }
}
