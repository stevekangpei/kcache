package com.kp.kcache.aop.support;

import com.kp.kcache.anno_config.CacheEvictAnnoConfig;
import com.kp.kcache.anno_config.CacheUpdateAnnoConfig;
import com.kp.kcache.anno_config.CacheableAnnoConfig;
import com.kp.kcache.anno_config.PenetrationConfig;
import com.kp.kcache.annos.AllowPenetration;
import com.kp.kcache.annos.CacheEvict;
import com.kp.kcache.annos.CacheUpdate;
import com.kp.kcache.annos.Cacheable;

import java.lang.reflect.Method;

/**
 * description: AnnoConfigUtils <br>
 * date: 2021/11/1 10:42 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class AnnoConfigUtils {

    public static CacheableAnnoConfig parseCacheableAnnoConfig(Method method,
                                                               Cacheable cacheable,
                                                               AllowPenetration allowPenetration) {
        CacheableAnnoConfig config = new CacheableAnnoConfig();

        config.setArea(cacheable.area());
        config.setName(cacheable.name());
        config.setTimeUnit(cacheable.timeUnit());
        config.setExpire(cacheable.expire());
        config.setLocalExpire(cacheable.localExpire());
        config.setCacheType(cacheable.cacheType());
        config.setKey(cacheable.key());
        config.setCacheNullValues(cacheable.cacheNullValues());
        config.setLocalLimit(cacheable.localLimit());

        if (allowPenetration == null) return config;

        PenetrationConfig penetrationConfig = new PenetrationConfig();
        penetrationConfig.setTimeOut(allowPenetration.timeOut());
        penetrationConfig.setTimeUnit(allowPenetration.timeUnit());
        config.setPenetrationConfig(penetrationConfig);

        return config;
    }

    public static CacheEvictAnnoConfig parseCacheEvictConfig(Method method,
                                                             CacheEvict cacheEvict) {
        CacheEvictAnnoConfig config = new CacheEvictAnnoConfig();
        config.setArea(cacheEvict.area());
        config.setName(cacheEvict.name());
        config.setCacheType(cacheEvict.cacheType());
        config.setKey(cacheEvict.key());

        return config;
    }

    public static CacheUpdateAnnoConfig parseCacheUpdateConfig(Method method,
                                                               CacheUpdate cacheUpdate) {
        CacheUpdateAnnoConfig config = new CacheUpdateAnnoConfig();
        config.setArea(cacheUpdate.area());
        config.setName(cacheUpdate.name());
        config.setCacheType(cacheUpdate.cacheType());
        config.setKey(cacheUpdate.key());

        return config;
    }

}
