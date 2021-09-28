package com.kp.kcache.autoconfigure.autoconfiguration;

import com.kp.cache_core.cache_builder.AbstractCacheBuilder;
import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.cache_builder.embedded.EmbeddedCacheBuilder;
import com.kp.cache_core.cache_builder.external.ExternalCacheBuilder;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.support.AbstractDecoder;
import com.kp.cache_core.support.AbstractEncoder;
import com.kp.cache_core.support.DecodeFactory;
import com.kp.cache_core.support.EncodeFactory;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import com.kp.kcache.autoconfigure.support.CacheContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

/**
 * 用于初始化Cache。
 * 在初始化bean的时候， 当条件满足的时候。
 * 即比如说满足CaffeineCondition 的时候。
 * 初始化这个Initializer。 用于从配置文件中读取配置信息。
 * 然后创建Cache。
 * description: AbstractCacheInitializer <br>
 * date: 2021/9/23 7:51 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheInitializer implements InitializingBean {

    @Autowired
    protected ConfigurableEnvironment environment;
    @Autowired
    private CacheContainer cacheContainer;

    private volatile boolean initted;

    private String[] cacheNames;

    public AbstractCacheInitializer(String... cacheNames) {
        this.cacheNames = cacheNames;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!initted) {
            parseCache("kcache.local.", true);
            parseCache("kcache.remote.", false);
            initted = true;
        }
    }

    private void parseCache(String prefix, boolean isLocal) {
        Set<String> cacheNames = Arrays.stream(this.cacheNames).collect(Collectors.toSet());
        CacheConfigTree cacheConfigTree = new CacheConfigTree(environment, prefix);
        Set<String> cacheAreas = getCacheAreas(prefix);

        for (String cacheArea : cacheAreas) {
            String cacheType = (String) cacheConfigTree.getProperty(cacheArea + ".cache_type");
            if (!cacheNames.contains(cacheType)) continue;

            // 获取subTree，即找到每个cache 的配置信息
            CacheConfigTree subTree = cacheConfigTree.subTree(cacheArea + "." + cacheType);
            ICacheBuilder cacheBuilder = buildCache(subTree);
            if (isLocal) {
                cacheContainer.addEmbeddedContainer(cacheType, (EmbeddedCacheBuilder) cacheBuilder);
            } else {
                cacheContainer.addExternalContainer(cacheType, (ExternalCacheBuilder) cacheBuilder);
            }
        }
    }

    private Set<String> getCacheAreas(String prefix) {
        Set<String> res = new HashSet<>();

        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                for (String name : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                    String subName = name.substring(prefix.length());
                    int index = subName.indexOf(".");
                    if (index < 0) continue;
                    res.add(subName.substring(0, index));
                }
            }
        }
        return res;
    }

    /**
     * 解析基本的config信息，即Embedded 和 External 的 公共配置信息，并封装到CacheBuilder的config中
     *
     * @param configTree
     * @param cacheBuilder
     */
    protected void parseBasicConfigInfo(CacheConfigTree configTree, ICacheBuilder cacheBuilder) {
        AbstractCacheBuilder abstractCacheBuilder = (AbstractCacheBuilder) cacheBuilder;

        String encoderKey = (String) configTree.getProperty("encoder");
        String decoderKey = (String) configTree.getProperty("decoder");
        AbstractEncoder encoder = EncodeFactory.getEncoderByKey(encoderKey);
        AbstractDecoder decoder = DecodeFactory.getDecoderByKey(decoderKey);
        if (encoder == null || decoder == null) {
            throw new CacheException("encoder or decoder should not be null, must be defined");
        }
        abstractCacheBuilder.getConfig().setEncoder(encoder);
        abstractCacheBuilder.getConfig().setDecoder(decoder);
        long expireAfterWrite = parseLong((String) configTree.getProperty("expireAfterWrite", Integer.MAX_VALUE));
        long expireAfterAccess = parseLong((String) configTree.getProperty("expireAfterAccess", Integer.MAX_VALUE));
        boolean cacheNullValues = Boolean.parseBoolean((String) configTree.getProperty("cacheNullValues", "false"));

        abstractCacheBuilder.getConfig().setExpireAfterWriteInMillis(expireAfterWrite);
        abstractCacheBuilder.getConfig().setExpireAfterAccess(expireAfterAccess);
        abstractCacheBuilder.getConfig().setCacheNullValues(cacheNullValues);
    }

    /**
     * 从configTree里面读取配置，然后初始化CacheBuilder存储起来
     *
     * @param configTree
     * @return
     */
    protected abstract ICacheBuilder buildCache(CacheConfigTree configTree);
}
