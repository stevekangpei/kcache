package com.kp.cache_core.embedded;

import com.kp.cache_core.core.AbstractCache;
import com.kp.cache_core.core.CacheResult;
import com.kp.cache_core.core.CacheValueHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * description: AbstractEmbeddedCache <br>
 * date: 2021/9/12 4:55 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractEmbeddedCache<K, V> extends AbstractCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractEmbeddedCache.class);

    private InnerMap innerMap;
    private EmbeddedCacheConfig<K, V> config;

    public AbstractEmbeddedCache(EmbeddedCacheConfig<K, V> config) {
        this.config = config;
        this.innerMap = initInnerCache();
    }

    protected abstract InnerMap initInnerCache();

    /**
     * @param k
     * @return
     */
    protected Object buildKey(K k) {
        Function<Object, Object> keyConvertor = config.getKeyConvertor();
        if (keyConvertor == null) return k;
        return keyConvertor.apply(k);
    }

    /**
     * 存储进去的是ValueHolder， 取出来的时候，需要先强转为ValueHolder
     *
     * @param k
     * @param v
     * @param expireAfterWrite
     * @param timeUnit
     * @return
     */
    @Override
    protected CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        CacheValueHolder holder = new CacheValueHolder(v, timeUnit.toMillis(expireAfterWrite));
        Object key = buildKey(k);
        this.innerMap.putValue(key, holder);
        return CacheResult.SUCCESS_WITHOUT_MSG;
    }

    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        Map<Object, CacheValueHolder> data = new HashMap<>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            Object k = buildKey(key);
            CacheValueHolder holder = new CacheValueHolder(value, timeUnit.toMillis(expireAfterWrite));
            data.put(k, holder);
        }
        innerMap.putAllValues(data);
        return CacheResult.SUCCESS_WITHOUT_MSG;
    }

    @Override
    public void close() throws IOException {

    }
}
