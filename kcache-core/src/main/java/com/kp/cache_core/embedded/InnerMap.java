package com.kp.cache_core.embedded;

import java.util.Collection;
import java.util.Map;

/**
 * description: InnerMap <br>
 * date: 2021/9/12 4:56 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface InnerMap {

    Object getValue(Object key);

    Map getAllValues(Collection keys);

    void putValue(Object key, Object value);

    void putAllValues(Map map);

    boolean removeValue(Object key);

    boolean putIfAbsentValue(Object key, Object value);

    void removeAllValues(Collection keys);

}
