package com.kp.cache_core.cache_builder;

import com.kp.cache_core.core.Cache;
import com.kp.cache_core.core.CacheConfig;

/**
 * description: ICacheBuilder <br>
 * date: 2021/9/15 7:56 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface ICacheBuilder {

    <K, V> Cache<K, V> buildCache(CacheConfig<K, V> config);
}
