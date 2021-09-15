package com.kp.cache_core.core;

import com.kp.cache_core.exception.CacheException;

import java.time.Duration;
import java.util.function.Function;

/**
 * description: CacheConfig <br>
 * date: 2021/9/12 5:42 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheConfig<K, V> implements Cloneable {

    private static final int DEFAULT_MAX_EXPIRE = Integer.MAX_VALUE;

    private long expireAfterWriteInMillis = DEFAULT_MAX_EXPIRE * 1000;
    private long expireAfterAccess;
    private Function<Object, Object> keyConvertor;

    private boolean cacheNullValues;

    /**
     * 是否允许并发加载
     */
    private boolean isPenetrationProtect = false;
    /**
     * 如果并发加载的话，默认的超时时间是多少。
     */
    private Duration penetrationTimeOut;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CacheException(e);
        }
    }

    public long getExpireAfterWriteInMillis() {
        return expireAfterWriteInMillis;
    }

    public boolean isExpireAfterAccess() {
        return expireAfterAccess > 0;
    }

    public void setExpireAfterWriteInMillis(long expireAfterWriteInMillis) {
        this.expireAfterWriteInMillis = expireAfterWriteInMillis;
    }

    public long getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public void setExpireAfterAccess(long expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    public Function<Object, Object> getKeyConvertor() {
        return keyConvertor;
    }

    public void setKeyConvertor(Function<Object, Object> keyConvertor) {
        this.keyConvertor = keyConvertor;
    }

    public boolean isCacheNullValues() {
        return cacheNullValues;
    }

    public void setCacheNullValues(boolean cacheNullValues) {
        this.cacheNullValues = cacheNullValues;
    }

    public boolean isPenetrationProtect() {
        return isPenetrationProtect;
    }

    public void setPenetrationProtect(boolean penetrationProtect) {
        isPenetrationProtect = penetrationProtect;
    }

    public Duration getPenetrationTimeOut() {
        return penetrationTimeOut;
    }

    public void setPenetrationTimeOut(Duration penetrationTimeOut) {
        this.penetrationTimeOut = penetrationTimeOut;
    }
}
