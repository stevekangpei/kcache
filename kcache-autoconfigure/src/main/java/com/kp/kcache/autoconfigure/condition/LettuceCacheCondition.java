package com.kp.kcache.autoconfigure.condition;

/**
 * description: LettuceCacheCondition <br>
 * date: 2021/9/23 7:50 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCacheCondition extends AbstractCacheCondition {

    public LettuceCacheCondition() {
        super("redis.lettuce");
    }
}
