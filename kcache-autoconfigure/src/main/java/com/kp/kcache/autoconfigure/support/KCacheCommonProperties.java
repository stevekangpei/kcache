package com.kp.kcache.autoconfigure.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description: KCacheCommonProperties <br>
 * date: 2021/10/16 10:44 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@ConfigurationProperties("kcache")
public class KCacheCommonProperties {

    /**
     * 是否开启保护模式（缓存未命中，是否只允许一个线程加载原方法，其他线程等待）
     */
    private boolean penetrationProtect = false;
    /**
     * 是否开启缓存
     */
    private boolean enableMethodCache = true;

    public boolean isPenetrationProtect() {
        return penetrationProtect;
    }

    public void setPenetrationProtect(boolean penetrationProtect) {
        this.penetrationProtect = penetrationProtect;
    }

    public boolean isEnableMethodCache() {
        return enableMethodCache;
    }

    public void setEnableMethodCache(boolean enableMethodCache) {
        this.enableMethodCache = enableMethodCache;
    }
}
