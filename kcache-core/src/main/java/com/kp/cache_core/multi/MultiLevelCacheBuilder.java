package com.kp.cache_core.multi;

import com.kp.cache_core.cache_builder.AbstractCacheBuilder;
import com.kp.cache_core.core.Cache;

/**
 * description: MultiLevelCacheBuilder <br>
 * date: 2021/11/3 10:59 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class MultiLevelCacheBuilder extends AbstractCacheBuilder {

    public MultiLevelCacheBuilder() {
        this.setBuildFunc(cacheConfig -> new MultiLevelCache((MultiLevelCacheConfig) cacheConfig));
    }

    @Override
    public MultiLevelCacheConfig getConfig() {
        if (config == null)
            return new MultiLevelCacheConfig();
        return (MultiLevelCacheConfig) config;
    }

    public MultiLevelCacheBuilder addCache(Cache... caches) {
        for (Cache cache : caches) {
            getConfig().getCaches().add(cache);
        }
        return this;
    }
}
