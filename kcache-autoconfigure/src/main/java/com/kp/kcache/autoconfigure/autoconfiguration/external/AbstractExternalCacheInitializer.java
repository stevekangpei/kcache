package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.kcache.autoconfigure.autoconfiguration.AbstractCacheInitializer;

/**
 * description: AbstractExternalCacheInitializer <br>
 * date: 2021/9/23 7:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractExternalCacheInitializer extends AbstractCacheInitializer {

    public AbstractExternalCacheInitializer(String... cacheNames) {
        super(cacheNames);
    }
}
