package com.kp.kcache.anno_config;

import java.util.concurrent.TimeUnit;

/**
 * description: CacheableAnnoConfig <br>
 * date: 2021/11/1 7:47 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheableAnnoConfig extends CacheAnnoConfig {

    private TimeUnit timeUnit;

    private int expire;

    private int localExpire;


    private boolean cacheNullValues;

    private int localLimit;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getLocalExpire() {
        return localExpire;
    }

    public void setLocalExpire(int localExpire) {
        this.localExpire = localExpire;
    }

    public boolean isCacheNullValues() {
        return cacheNullValues;
    }

    public void setCacheNullValues(boolean cacheNullValues) {
        this.cacheNullValues = cacheNullValues;
    }

    public int getLocalLimit() {
        return localLimit;
    }

    public void setLocalLimit(int localLimit) {
        this.localLimit = localLimit;
    }
}
