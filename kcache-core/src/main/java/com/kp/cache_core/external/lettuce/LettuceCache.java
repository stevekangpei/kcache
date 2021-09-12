package com.kp.cache_core.external.lettuce;

import com.kp.cache_core.core.CacheResult;
import com.kp.cache_core.external.AbstractExternalCache;
import com.kp.cache_core.external.ExternalCacheConfig;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: LettuceCache <br>
 * date: 2021/9/12 6:19 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCache<K, V> extends AbstractExternalCache<K, V> {

    private ExternalCacheConfig config;

    public LettuceCache(ExternalCacheConfig<K, V> config) {
        super(config);
        this.config = config;
    }

    @Override
    protected CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
