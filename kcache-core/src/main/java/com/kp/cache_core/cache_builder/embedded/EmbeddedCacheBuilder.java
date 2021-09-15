package com.kp.cache_core.cache_builder.embedded;

import com.kp.cache_core.cache_builder.AbstractCacheBuilder;
import com.kp.cache_core.embedded.EmbeddedCacheConfig;

/**
 * description: EmbeddedCacheBuilder <br>
 * date: 2021/9/15 10:16 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class EmbeddedCacheBuilder extends AbstractCacheBuilder {


    @Override
    public EmbeddedCacheConfig getConfig() {
        if (config == null) {
            return new EmbeddedCacheConfig();
        }
        return (EmbeddedCacheConfig) config;
    }

    public void setLimit(int limit) {
        getConfig().setLimit(limit);
    }
}
