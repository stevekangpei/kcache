package com.kp.kcache.autoconfigure.autoconfiguration.embedded;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.cache_builder.embedded.CaffeineCacheBuilder;
import com.kp.kcache.autoconfigure.condition.CaffeineCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * description: CaffeineCacheInitializer <br>
 * date: 2021/9/23 7:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
@Conditional(CaffeineCondition.class)
public class CaffeineCacheInitializer extends AbstractEmbeddedCacheInitializer {

    public CaffeineCacheInitializer() {
        super("caffeine");
    }

    @Override
    protected ICacheBuilder buildCache(CacheConfigTree configTree) {
        CaffeineCacheBuilder caffeineCacheBuilder = new CaffeineCacheBuilder();
        super.parseBasicConfigInfo(configTree, caffeineCacheBuilder);
        return caffeineCacheBuilder;
    }
}
