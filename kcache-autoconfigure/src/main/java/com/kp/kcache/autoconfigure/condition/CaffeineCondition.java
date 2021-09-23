package com.kp.kcache.autoconfigure.condition;

/**
 * description: CaffeineCondition <br>
 * date: 2021/9/23 7:48 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CaffeineCondition extends AbstractCacheCondition {

    public CaffeineCondition() {
        super("caffeine");
    }
}
