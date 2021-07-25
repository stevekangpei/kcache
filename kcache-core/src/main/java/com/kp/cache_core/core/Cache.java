package com.kp.cache_core.core;

import java.io.Closeable;

/**
 * 1, GET/GEL_ALL
 * 2, PUT/PUL_ALL
 * 3, DELETE/DELETE_ALL
 * 4,
 * description: Cache <br>
 * date: 2021/7/25 10:05 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface Cache<K, V> extends Closeable {

    V GET(K k);
}
