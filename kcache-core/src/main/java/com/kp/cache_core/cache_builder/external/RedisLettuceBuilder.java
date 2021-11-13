package com.kp.cache_core.cache_builder.external;

import com.kp.cache_core.external.lettuce.LettuceCache;
import com.kp.cache_core.external.lettuce.LettuceCacheConfig;

/**
 * description: RedieLettuceConfig <br>
 * date: 2021/9/15 10:37 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class RedisLettuceBuilder extends ExternalCacheBuilder {

    public RedisLettuceBuilder() {
        this.setBuildFunc(cacheConfig -> new LettuceCache((LettuceCacheConfig) cacheConfig));
    }

    @Override
    public LettuceCacheConfig getConfig() {
        if (config == null) {
            config = new LettuceCacheConfig();
        }
        return (LettuceCacheConfig) config;
    }

}
