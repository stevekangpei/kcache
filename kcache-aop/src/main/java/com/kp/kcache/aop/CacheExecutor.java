package com.kp.kcache.aop;

import com.kp.cache_core.core.Cache;
import com.kp.cache_core.core.CacheLoader;
import com.kp.cache_core.exception.CacheException;
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

import java.lang.reflect.Array;
import java.util.*;

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

    public Object invokeOrigin(CacheContext cacheContext) throws Throwable {
        return cacheContext.getInvoker().invoke();
    }


    public void invokeCacheable(CacheContext cacheContext) throws Throwable {
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
        CacheLoader loader = key1 -> {
            Object result = invokeOrigin(cacheContext);
            cacheContext.setResult(result);
            return result;
        };

        Object o = cache.computeIfAbsent(key, loader);
        cacheContext.setResult(o);
    }


    /**
     * 首先通过 Container 获取 cacheable 实例。
     * 然后通过 实例初始化cache，如果没有cache的话。
     *
     * @param cacheContext
     */
    public void invokeUpdate(CacheContext cacheContext) throws Throwable {
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

        if (config.isMultiKeys()) {
            Iterable keys = toIterable(key);
            Iterable values = toIterable(res);

            List keyList = new ArrayList();
            List valueList = new ArrayList();
            keys.forEach(o -> keyList.add(o));
            values.forEach(o -> valueList.add(o));
            if (keyList.size() != valueList.size()) {
                logger.error("keys length and values length not equal");
                throw new CacheException("keys length and values length not equal");
            } else {
                Map m = new HashMap();
                for (int i = 0; i < valueList.size(); i++) {
                    m.put(keyList.get(i), valueList.get(i));
                }
                cache.putAll(m);
            }
        } else {
            cache.put(key, res);
        }
    }

    private static Iterable toIterable(Object obj) {
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return Arrays.asList((Object[]) obj);
            } else {
                List list = new ArrayList();
                int len = Array.getLength(obj);
                for (int i = 0; i < len; i++) {
                    list.add(Array.get(obj, i));
                }
                return list;
            }
        } else if (obj instanceof Iterable) {
            return (Iterable) obj;
        } else {
            return null;
        }
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

        if (config.isMultiKeys()) {
            Iterable it = toIterable(key);
            if (it == null) {
                logger.error("invoke evict error , key null");
                return;
            }
            Set keys = new HashSet();
            it.forEach(k -> keys.add(k));
            cache.deleteAll(new ArrayList(keys));
        } else {
            cache.delete(key);
        }
    }
}
