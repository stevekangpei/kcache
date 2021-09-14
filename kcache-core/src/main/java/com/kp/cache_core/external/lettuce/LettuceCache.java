package com.kp.cache_core.external.lettuce;

import com.kp.cache_core.core.CacheConfig;
import com.kp.cache_core.core.CacheGetResult;
import com.kp.cache_core.core.CacheResult;
import com.kp.cache_core.core.MultiCacheGetResult;
import com.kp.cache_core.external.AbstractExternalCache;
import com.kp.cache_core.external.ExternalCacheConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: LettuceCache <br>
 * date: 2021/9/12 6:19 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCache<K, V> extends AbstractExternalCache<K, V> {

    private ExternalCacheConfig<K, V> config;

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
