package com.kp.kcache.autoconfigure.condition;

/**
 * description: RedisCacheCondition <br>
 * date: 2021/9/23 7:49 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class RedisCacheCondition extends AbstractCacheCondition {

    public RedisCacheCondition() {
        super("redis");
    }
}
