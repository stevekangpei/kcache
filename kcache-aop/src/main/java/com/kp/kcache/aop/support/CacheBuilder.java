package com.kp.kcache.aop.support;

import com.kp.cache_core.cache_builder.embedded.EmbeddedCacheBuilder;
import com.kp.cache_core.cache_builder.external.ExternalCacheBuilder;
import com.kp.cache_core.core.Cache;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.multi.MultiLevelCacheBuilder;
import com.kp.kcache.anno_config.*;
import com.kp.kcache.annos.CacheType;
import com.kp.kcache.autoconfigure.support.GlobalCacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * description: CacheBuilder <br>
 * date: 2021/11/3 8:02 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
public class CacheBuilder {

    @Autowired
    private AnnoConfigContainer annoConfigContainer;

    @Autowired
    private GlobalCacheConfig globalCacheConfig;

    public BiFunction<CacheContext, CacheAnnoConfig, Cache> buildCache() {
        return (context, config) -> {
            if (config.getCache() != null) return config.getCache();

            String area = config.getArea();
            String name = config.getName();

            if (StringUtils.isEmpty(name)) {
                name = DefaultCacheNameGenerator.generateCacheName(context.getMethod(),
                        context.getTargetObject());
            }
            CacheableAnnoConfig cacheableAnnoConfig;
            if (config instanceof CacheEvictAnnoConfig || config instanceof CacheUpdateAnnoConfig) {
                cacheableAnnoConfig = annoConfigContainer.getCacheableConfigByAreaAndName(config.getArea(), config.getName());
            } else {
                cacheableAnnoConfig = (CacheableAnnoConfig) config;
            }

            Cache cache = CacheContainer.getCacheByAreaAndName(area, name);
            if (cache == null) {
                synchronized (this) {
                    cache = CacheContainer.getCacheByAreaAndName(area, name);
                    if (cache == null) {
                        cache = buildCache(cacheableAnnoConfig, name);
                    }
                }
            }
            CacheContainer.addCache(area, name, cache);
            config.setCache(cache);
            return cache;
        };
    }

    private Cache buildCache(CacheableAnnoConfig cacheableAnnoConfig, String name) {
        if (cacheableAnnoConfig.getCacheType() == CacheType.LOCAL) {
            return buildLocalCache(cacheableAnnoConfig);
        } else if (cacheableAnnoConfig.getCacheType() == CacheType.REMOTE) {
            return buildRemoteCache(cacheableAnnoConfig, name);
        } else {
            Cache local = buildLocalCache(cacheableAnnoConfig);
            Cache remote = buildRemoteCache(cacheableAnnoConfig, name);
            return new MultiLevelCacheBuilder().addCache(local, remote).buildCache();
        }
    }

    private Cache buildLocalCache(CacheableAnnoConfig cacheableAnnoConfig) {
        String area = cacheableAnnoConfig.getArea();
        EmbeddedCacheBuilder embeddedCacheBuilder = globalCacheConfig.getLocalCacheBuilders().get(area);
        if (embeddedCacheBuilder == null) throw new CacheException("no embedded cache builder");
        embeddedCacheBuilder = (EmbeddedCacheBuilder) embeddedCacheBuilder.clone();

        if (cacheableAnnoConfig.getLocalLimit() > 0) {
            embeddedCacheBuilder.setLimit(cacheableAnnoConfig.getLocalLimit());
        }
        if (cacheableAnnoConfig.getLocalExpire() > 0) {
            TimeUnit timeUnit = cacheableAnnoConfig.getTimeUnit();
            embeddedCacheBuilder.getConfig().setExpireAfterWriteInMillis(timeUnit.toMillis(cacheableAnnoConfig.getLocalExpire()));
        }
        embeddedCacheBuilder.getConfig().setCacheNullValues(cacheableAnnoConfig.isCacheNullValues());
        if (cacheableAnnoConfig.getPenetrationConfig() != null) {
            embeddedCacheBuilder.getConfig().setPenetrationProtect(true);
            PenetrationConfig config = cacheableAnnoConfig.getPenetrationConfig();
            embeddedCacheBuilder.getConfig().setPenetrationTimeOut(Duration.ofMillis(config.getTimeUnit()
                    .toMillis(config.getTimeOut())));
        }
        return embeddedCacheBuilder.buildCache();
    }

    private Cache buildRemoteCache(CacheableAnnoConfig config, String name) {
        String area = config.getArea();
        ExternalCacheBuilder externalCacheBuilder = globalCacheConfig.getRemoteCacheBuilders().get(area);
        if (externalCacheBuilder == null) throw new CacheException("no external cache builder");
        externalCacheBuilder = (ExternalCacheBuilder) externalCacheBuilder.clone();

        if (config.getExpire() > 0) {
            TimeUnit timeUnit = config.getTimeUnit();
            externalCacheBuilder.getConfig().setExpireAfterWriteInMillis(timeUnit.toMillis(config.getExpire()));
        }
        if (!StringUtils.isEmpty(name)) {
            externalCacheBuilder.getConfig().setKeyPrefix(name);
        }
        externalCacheBuilder.getConfig().setCacheNullValues(config.isCacheNullValues());
        if (config.getPenetrationConfig() != null) {
            externalCacheBuilder.getConfig().setPenetrationProtect(true);
            PenetrationConfig penetrationConfig = config.getPenetrationConfig();
            externalCacheBuilder.getConfig().setPenetrationTimeOut(Duration.ofMillis(penetrationConfig.getTimeUnit()
                    .toMillis(penetrationConfig.getTimeOut())));
        }
        return externalCacheBuilder.buildCache();
    }
}
