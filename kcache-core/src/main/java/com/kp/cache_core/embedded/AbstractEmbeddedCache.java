package com.kp.cache_core.embedded;

import com.kp.cache_core.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Object buildKey(K k) {
        Function<Object, Object> keyConvertor = config.getKeyConvertor();
        if (keyConvertor == null) return k;
        return keyConvertor.apply(k);
    }

    @Override
    public CacheConfig<K, V> config() {
        return config;
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
    protected CacheGetResult<V> do_Get(K k) {
        Object key = buildKey(k);
        CacheValueHolder value = (CacheValueHolder) innerMap.getValue(key);
        return parseGetResult(value);
    }

    protected CacheGetResult<V> parseGetResult(CacheValueHolder value) {
        long now = System.currentTimeMillis();

        if (value == null) {
            return new CacheGetResult<>(ResultCode.NOT_EXIST, "not exist");
        } else if (now > value.getExpireTime()) {
            return new CacheGetResult<>(ResultCode.EXPIRED, "expired");
        } else if (config.isExpireAfterAccess()) {
            long expireAccessTime = value.getAccessTime() + config.getExpireAfterAccess();
            if (now > expireAccessTime) {
                return new CacheGetResult<>(ResultCode.EXPIRED, "expired");
            }
        }
        return new CacheGetResult<>(ResultCode.SUCCESS, "success", value);
    }

    @Override
    protected MultiCacheGetResult<K, V> do_Get_All(List<K> keys) {
        List<Object> newKeys = keys.stream().map(this::buildKey).collect(Collectors.toList());
        Map<K, CacheGetResult<V>> res = new HashMap<>(keys.size());

        for (int i = 0; i < newKeys.size(); i++) {
            Object key = newKeys.get(i);
            K k = keys.get(i);
            CacheValueHolder value = (CacheValueHolder) innerMap.getValue(key);
            CacheGetResult<V> v = parseGetResult(value);
            res.put(k, v);
        }
        return new MultiCacheGetResult<>(ResultCode.SUCCESS, "", res);
    }

    @Override
    protected CacheResult do_Delete_All(List<K> keys) {
        Set newKeys = keys.stream().map(this::buildKey).collect(Collectors.toSet());
        innerMap.removeAllValues(newKeys);
        return CacheResult.SUCCESS_WITHOUT_MSG;
    }

    @Override
    protected CacheResult do_Delete(K k) {
        boolean b = innerMap.removeValue(buildKey(k));
        return b ? CacheResult.SUCCESS_WITHOUT_MSG : CacheResult.FAIL_WITHOUT_MSG;
    }

    @Override
    public void close() throws IOException {

    }
}
