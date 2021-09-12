package com.kp.cache_core.external;

import com.kp.cache_core.core.CacheConfig;

import java.util.function.Function;

/**
 * description: ExternalCacheConfig <br>
 * date: 2021/9/12 6:02 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class ExternalCacheConfig<K, V> extends CacheConfig<K, V> {

    private String keyPrefix;
    private Function<Object, byte[]> encoder;
    private Function<byte[], Object> decoder;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Function<Object, byte[]> getEncoder() {
        return encoder;
    }

    public void setEncoder(Function<Object, byte[]> encoder) {
        this.encoder = encoder;
    }

    public Function<byte[], Object> getDecoder() {
        return decoder;
    }

    public void setDecoder(Function<byte[], Object> decoder) {
        this.decoder = decoder;
    }

    public ExternalCacheConfig() {
    }
}
