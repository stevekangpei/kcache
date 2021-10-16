package com.kp.cache_core.external;

import com.kp.cache_core.core.CacheConfig;
import com.kp.cache_core.support.AbstractDecoder;
import com.kp.cache_core.support.AbstractEncoder;

/**
 * description: ExternalCacheConfig <br>
 * date: 2021/9/12 6:02 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class ExternalCacheConfig<K, V> extends CacheConfig<K, V> {

    private String keyPrefix;
    private AbstractEncoder encoder;
    private AbstractDecoder decoder;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public AbstractEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(AbstractEncoder encoder) {
        this.encoder = encoder;
    }

    public AbstractDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(AbstractDecoder decoder) {
        this.decoder = decoder;
    }

    public ExternalCacheConfig() {
    }
}
