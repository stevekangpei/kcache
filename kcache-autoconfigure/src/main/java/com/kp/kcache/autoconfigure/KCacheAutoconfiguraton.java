package com.kp.kcache.autoconfigure;

import com.kp.kcache.autoconfigure.autoconfiguration.embedded.CaffeineCacheInitializer;
import com.kp.kcache.autoconfigure.autoconfiguration.external.RedisCacheInitializer;
import com.kp.kcache.autoconfigure.autoconfiguration.external.RedisLettuceCacheInitializer;
import com.kp.kcache.autoconfigure.support.BeanDefinitionMetadataManager;
import com.kp.kcache.autoconfigure.support.CacheBuilderContainer;
import com.kp.kcache.autoconfigure.support.GlobalCacheConfig;
import com.kp.kcache.autoconfigure.support.KCacheCommonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * description: KCacheAutoconfiguraton <br>
 * date: 2021/9/28 10:45 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Configuration
@EnableConfigurationProperties({KCacheCommonProperties.class})
@Import({CaffeineCacheInitializer.class,
        RedisCacheInitializer.class,
        RedisLettuceCacheInitializer.class
})
@EnableAspectJAutoProxy
public class KCacheAutoconfiguraton {
    public static final String cacheInitializerName = "cache.globalCacheConfig";

    @Bean
    public BeanDefinitionMetadataManager beanDefinitionMetadataManager() {
        return new BeanDefinitionMetadataManager();
    }

    @Bean
    public CacheBuilderContainer cacheBuilderContainer() {
        return new CacheBuilderContainer();
    }

    @Bean(name = cacheInitializerName)
    public GlobalCacheConfig globalCacheConfig(CacheBuilderContainer container,
                                               KCacheCommonProperties properties) {
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setLocalCacheBuilders(container.getAllEmbeddedBuilders());
        globalCacheConfig.setRemoteCacheBuilders(container.getAllExternalBuilders());
        globalCacheConfig.setEnableMethodCache(properties.isEnableMethodCache());
        globalCacheConfig.setPenetrationProtect(properties.isPenetrationProtect());

        return globalCacheConfig;
    }
}
