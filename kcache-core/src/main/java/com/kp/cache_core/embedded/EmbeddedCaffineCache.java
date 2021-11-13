package com.kp.cache_core.embedded;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.kp.cache_core.core.CacheValueHolder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: CaffineCache <br>
 * date: 2021/9/12 5:08 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class EmbeddedCaffineCache extends AbstractEmbeddedCache {

    private com.github.benmanes.caffeine.cache.Cache cache;
    private EmbeddedCacheConfig config;

    public EmbeddedCaffineCache(EmbeddedCacheConfig config) {
        super(config);
    }

    @Override
    protected InnerMap initInnerCache() {
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        builder.maximumSize(config.getLimit());
        boolean isExpireAfterAccess = config.isExpireAfterAccess();
        long expireAfterAccess = config.getExpireAfterAccess();

        builder.expireAfter(new Expiry<Object, CacheValueHolder>() {
            @Override
            public long expireAfterCreate(@NonNull Object o, @NonNull CacheValueHolder o2, long l) {
                return getRestTimeInNanos(o2);
            }

            @Override
            public long expireAfterUpdate(@NonNull Object o, @NonNull CacheValueHolder o2, long l, @NonNegative long l1) {
                return l1;
            }

            @Override
            public long expireAfterRead(@NonNull Object o, @NonNull CacheValueHolder o2, long l, @NonNegative long l1) {
                return getRestTimeInNanos(o2);
            }

            private long getRestTimeInNanos(CacheValueHolder value) {
                long now = System.currentTimeMillis();
                long ttl = value.getExpireTime() - now;
                /*
                 * 如果本地缓存设置了多长时间没访问缓存则失效
                 */
                if (isExpireAfterAccess) {
                    // 设置缓存的失效时间
                    // 多长时间没访问缓存则失效 and 缓存的有效时长取 min
                    ttl = Math.min(ttl, expireAfterAccess);
                }
                return TimeUnit.MILLISECONDS.toNanos(ttl);
            }

        });
        this.cache = builder.build();
        return new InnerMap() {
            @Override
            public Object getValue(Object key) {
                return cache.getIfPresent(key);
            }

            @Override
            public Map getAllValues(Collection keys) {
                return cache.getAllPresent(keys);
            }

            @Override
            public void putValue(Object key, Object value) {
                cache.put(key, value);
            }

            @Override
            public void putAllValues(Map map) {
                cache.putAll(map);
            }

            @Override
            public boolean removeValue(Object key) {
                return cache.asMap().remove(key) != null;
            }

            @Override
            public boolean putIfAbsentValue(Object key, Object value) {
                return cache.asMap().putIfAbsent(key, value) == null;
            }

            @Override
            public void removeAllValues(Collection keys) {
                cache.invalidateAll(keys);
            }
        };
    }
}
