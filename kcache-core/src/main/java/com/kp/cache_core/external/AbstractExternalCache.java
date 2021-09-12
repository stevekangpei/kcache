package com.kp.cache_core.external;

import com.kp.cache_core.core.AbstractCache;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.utils.KeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: AbstractExternalCache <br>
 * date: 2021/9/12 5:40 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractExternalCache<K, V> extends AbstractCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractExternalCache.class);

    private ExternalCacheConfig<K, V> config;

    public AbstractExternalCache(ExternalCacheConfig<K, V> config) {
        this.config = config;
        checkConfig();
    }

    private void checkConfig() {
        if (this.config.getDecoder() == null) throw new CacheException("external cache decoder is null");
        if (this.config.getEncoder() == null) throw new CacheException("external cache encoder is null");
        if (this.config.getKeyPrefix() == null) throw new CacheException("prefix in external cache should not be null");
    }

    public byte[] buildKey(K k) {
        Object newKey;
        try {
            if (k instanceof byte[] || k instanceof String) {
                newKey = k;
            } else {
                newKey = config.getEncoder().apply(k);
            }
            return KeyUtils.buildKeyAfterConvert(newKey, config.getKeyPrefix());
        } catch (Exception e) {
            logger.error("build key for external cache error k {}", k);
            throw new CacheException(e);
        }
    }
}
