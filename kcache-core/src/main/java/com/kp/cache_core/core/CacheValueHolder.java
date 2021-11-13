package com.kp.cache_core.core;

/**
 * description: CacheValueHolder <br>
 * date: 2021/9/12 5:00 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheValueHolder {

    private Object data;

    private long accessTime;

    private long expireTime;

    public CacheValueHolder(Object data, long accessTime, long expireTime) {
        this.data = data;
        this.accessTime = accessTime;
        this.expireTime = expireTime;
    }

    public CacheValueHolder(Object data, long expireTime) {
        this.data = data;
        this.accessTime = System.currentTimeMillis();
        this.expireTime = this.accessTime + expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
