package com.kp.kcache.autoconfigure.autoconfiguration;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import com.kp.kcache.autoconfigure.support.CacheContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;

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
    private ConfigurableEnvironment environment;
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

            initted = true;
        }
    }

    private void parseCache(String prefix) {

    }

    protected void parseBasicConfigInfo(CacheConfigTree configTree, String prefix, ICacheBuilder cacheBuilder) {

    }

    protected abstract ICacheBuilder buildCache(CacheConfigTree configTree);
}
