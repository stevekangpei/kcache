package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.kcache.autoconfigure.condition.RedisCacheCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * description: RedisCacheInitializer <br>
 * date: 2021/9/23 7:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
@Conditional(RedisCacheCondition.class)
public class RedisCacheInitializer extends AbstractExternalCacheInitializer {

    public RedisCacheInitializer() {
        super("redis");
    }

    @Override
    protected ICacheBuilder buildCache(CacheConfigTree configTree) {
        return null;
    }
}
