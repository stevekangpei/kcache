package com.kp.kcache.autoconfigure.autoconfiguration.embedded;

import com.kp.kcache.autoconfigure.autoconfiguration.AbstractCacheInitializer;

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
}
