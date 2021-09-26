package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.kcache.autoconfigure.condition.LettuceCacheCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * description: RedisLettuceCacheInitializer <br>
 * date: 2021/9/23 7:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
@Conditional(LettuceCacheCondition.class)
public class RedisLettuceCacheInitializer extends AbstractExternalCacheInitializer {

    public RedisLettuceCacheInitializer() {
        super("lettuce");
    }

    @Override
    protected ICacheBuilder buildCache(CacheConfigTree configTree) {
        return null;
    }
}
