package com.kp.kcache.autoconfigure.autoconfiguration.embedded;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.cache_builder.embedded.EmbeddedCacheBuilder;
import com.kp.kcache.autoconfigure.autoconfiguration.AbstractCacheInitializer;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;

/**
 * description: AbstractEmbeddedCacheInitializer <br>
 * date: 2021/9/23 7:53 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractEmbeddedCacheInitializer extends AbstractCacheInitializer {

    public AbstractEmbeddedCacheInitializer(String... cacheNames) {
        super(cacheNames);
    }

    @Override
    protected void parseBasicConfigInfo(CacheConfigTree configTree, ICacheBuilder cacheBuilder) {
        super.parseBasicConfigInfo(configTree, cacheBuilder);
        EmbeddedCacheBuilder cacheBuilder1 = (EmbeddedCacheBuilder) cacheBuilder;
        int limit = (int) configTree.getProperty("limit", 2000);
        cacheBuilder1.getConfig().setLimit(limit);
    }
}
