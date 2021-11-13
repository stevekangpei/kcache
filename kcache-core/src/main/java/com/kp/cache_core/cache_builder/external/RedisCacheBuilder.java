package com.kp.cache_core.cache_builder.external;

import com.kp.cache_core.external.redis.RedisCache;
import com.kp.cache_core.external.redis.RedisCacheConfig;

/**
 * description: RedisCacheBuilder <br>
 * date: 2021/9/15 10:36 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class RedisCacheBuilder extends ExternalCacheBuilder {

    public RedisCacheBuilder() {
        this.setBuildFunc((cacheConfig) -> new RedisCache((RedisCacheConfig) cacheConfig));
    }

    @Override
    public RedisCacheConfig getConfig() {
        if (config == null) {
            config = new RedisCacheConfig();
        }
        return (RedisCacheConfig) config;
    }
}
