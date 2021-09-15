package com.kp.cache_core.cache_builder.external;

import com.kp.cache_core.cache_builder.AbstractCacheBuilder;
import com.kp.cache_core.external.ExternalCacheConfig;

import java.util.function.Function;

/**
 * description: ExternalCacheBuilder <br>
 * date: 2021/9/15 10:17 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class ExternalCacheBuilder extends AbstractCacheBuilder {

    @Override
    public ExternalCacheConfig getConfig() {
        if (config == null) {
            config = new ExternalCacheConfig();
        }
        return (ExternalCacheConfig) config;
    }

    public void setKeyPrefix(String keyPrefix) {
        getConfig().setKeyPrefix(keyPrefix);
    }

    public void setValueEncoder(Function<Object, byte[]> encoder) {
        getConfig().setEncoder(encoder);
    }

    public void setValueDecoder(Function<byte[], Object> decoder) {
        getConfig().setDecoder(decoder);
    }
}
