package com.kp.cache_core.cache_builder.embedded;

import com.kp.cache_core.embedded.EmbeddedCacheConfig;
import com.kp.cache_core.embedded.EmbeddedCaffineCache;

/**
 * description: CaffineCacheBuilder <br>
 * date: 2021/9/15 10:21 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CaffeineCacheBuilder extends EmbeddedCacheBuilder {

    public CaffeineCacheBuilder() {
        this.setBuildFunc((cacheConfig) -> new EmbeddedCaffineCache((EmbeddedCacheConfig) cacheConfig));
    }

}
