package com.kp.cache_core.multi;

import com.kp.cache_core.core.Cache;
import com.kp.cache_core.core.CacheConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * description: MultiLevelCacheConfig <br>
 * date: 2021/11/3 10:59 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class MultiLevelCacheConfig<K, V> extends CacheConfig<K, V> {

    private List<Cache<K, V>> caches = new ArrayList<>();

    @Override
    public MultiLevelCacheConfig clone() {
        MultiLevelCacheConfig copy = (MultiLevelCacheConfig) super.clone();
        if (caches != null) {
            copy.caches = new ArrayList(this.caches);
        }
        return copy;
    }

    public List<Cache<K, V>> getCaches() {
        return caches;
    }
    public void setCaches(List<Cache<K, V>> caches) {
        this.caches = caches;
    }


}
