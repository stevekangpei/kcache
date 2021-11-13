package com.kp.kcache.anno_config;

/**
 * description: CacheUpdateAnnoConfig <br>
 * date: 2021/11/1 7:47 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheUpdateAnnoConfig extends CacheAnnoConfig {
    private boolean multiKeys;

    public boolean isMultiKeys() {
        return multiKeys;
    }

    public void setMultiKeys(boolean multiKeys) {
        this.multiKeys = multiKeys;
    }

}
