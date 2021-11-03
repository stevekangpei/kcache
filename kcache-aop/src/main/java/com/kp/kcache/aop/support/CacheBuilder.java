package com.kp.kcache.aop.support;

import com.kp.cache_core.core.Cache;
import com.kp.kcache.anno_config.CacheAnnoConfig;

import java.util.function.BiFunction;

/**
 * description: CacheBuilder <br>
 * date: 2021/11/3 8:02 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheBuilder {

    public static BiFunction<CacheContext, CacheAnnoConfig, Cache> buildCache(CacheContext cacheContext,
                                                                              CacheAnnoConfig config) {

    }
}
