package com.kp.kcache.aop;

import com.kp.cache_core.core.Cache;
import com.kp.kcache.anno_config.CacheEvictAnnoConfig;
import com.kp.kcache.anno_config.CacheUpdateAnnoConfig;
import com.kp.kcache.anno_config.CacheableAnnoConfig;
import com.kp.kcache.aop.support.AnnoConfigContainer;
import com.kp.kcache.aop.support.CacheContext;
import com.kp.kcache.aop.support.SimpleKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description: CacheExecutor <br>
 * date: 2021/11/3 7:04 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
public class CacheExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CacheExecutor.class);

    @Autowired
    private SimpleKeyGenerator keyGenerator;

    @Autowired
    private AnnoConfigContainer configContainer;

    public Object invokeOrigin(CacheContext cacheContext) throws Exception {
        return cacheContext.getInvoker().invoke();
    }


    public void invokeCacheable(CacheContext cacheContext) throws Exception {
        CacheableAnnoConfig cacheAnnoConfig = (CacheableAnnoConfig) cacheContext.getCacheAnnoConfig();
        Cache cache = null;

        if (cacheAnnoConfig.getCache() == null) {
            cache = cacheContext.getCacheBuilder().apply(cacheContext, cacheAnnoConfig);
            if (cache == null) {
                logger.error("apply cache error, method {}", cacheContext.getMethod());
                cacheContext.setResult(invokeOrigin(cacheContext));
                return;
            }
            cacheAnnoConfig.setCache(cache);
        }
        Object key = keyGenerator.generate(cacheAnnoConfig.getKey(), cacheContext.getTargetObject(),
                cacheContext.getMethod(), cacheContext.getArgs());

        if (key == null) {
            cacheContext.setResult(invokeOrigin(cacheContext));
            return;
        }
        Object o = cache.get(key);
        cacheContext.setResult(o);
    }


    /**
     * 首先通过 Container 获取 cacheable 实例。
     * 然后通过 实例初始化cache，如果没有cache的话。
     *
     * @param cacheContext
     */
    public void invokeUpdate(CacheContext cacheContext) throws Exception {
        Object res = invokeOrigin(cacheContext);
        cacheContext.setResult(res);

        CacheUpdateAnnoConfig config = (CacheUpdateAnnoConfig) cacheContext.getCacheAnnoConfig();
        Cache cache = null;
        if (config.getCache() == null) {
            cache = cacheContext.getCacheBuilder().apply(cacheContext, config);
            config.setCache(cache);
        }
        if (cache == null) return;

        Object key = keyGenerator.generate(config.getKey(), cacheContext.getTargetObject(),
                cacheContext.getMethod(), cacheContext.getArgs());
        if (key == null) return;
        cache.put(key, res);
    }

    public void invokeEvict(CacheContext cacheContext) throws Throwable {
        Object res = invokeOrigin(cacheContext);
        cacheContext.setResult(res);

        CacheEvictAnnoConfig config = (CacheEvictAnnoConfig) cacheContext.getCacheAnnoConfig();
        Cache cache = null;
        if (config.getCache() == null) {
            cache = cacheContext.getCacheBuilder().apply(cacheContext, config);
            config.setCache(cache);
        }
        if (cache == null) return;

        Object key = keyGenerator.generate(config.getKey(), cacheContext.getTargetObject(),
                cacheContext.getMethod(), cacheContext.getArgs());
        if (key == null) return;
        cache.delete(key);
    }
}
