package com.kp.cache_core.core;


import com.kp.cache_core.exception.CacheException;

import java.util.function.Function;

/**
 * description: CacheLoader <br>
 * date: 2021/11/13 8:12 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@FunctionalInterface
public interface CacheLoader<K, V> extends Function<K, V> {

    V load(K key) throws Throwable;

    @Override
    default V apply(K k) {
        try {
            return load(k);
        } catch (Throwable throwable) {
            String s = String.format("load key %s error", k);
            throw new CacheException(s, throwable);
        }
    }
}
