package com.kp.cache_core.embedded;

import com.kp.cache_core.core.CacheConfig;

/**
 * description: EmbeddedCacheConfig <br>
 * date: 2021/9/12 5:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class EmbeddedCacheConfig<K, V> extends CacheConfig<K, V> {

    private int limit = 1000;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
