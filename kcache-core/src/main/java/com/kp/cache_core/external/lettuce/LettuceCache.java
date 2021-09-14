package com.kp.cache_core.external.lettuce;

import com.kp.cache_core.core.*;
import com.kp.cache_core.external.AbstractExternalCache;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisStringAsyncCommands;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
            CacheResult cacheResult = new CacheResult(future.handle((res, ex) -> {
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

            return cacheResult;
        } catch (Exception e) {
            return new CacheResult(e);
        }
    }


    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    protected CacheGetResult<V> do_Get(K k) {
        return null;
    }

    @Override
    protected MultiCacheGetResult<K, V> do_Get_All(List<K> keys) {
        return null;
    }

    @Override
    protected CacheResult do_Delete_All(List<K> keys) {
        return null;
    }

    @Override
    protected CacheResult do_Delete(K k) {
        return null;
    }

    @Override
    public CacheConfig<K, V> config() {
        return this.config;
    }

    @Override
    public void close() throws IOException {

    }
}
