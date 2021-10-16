package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.cache_builder.external.RedisCacheBuilder;
import com.kp.cache_core.exception.CacheException;
import com.kp.kcache.autoconfigure.condition.RedisCacheCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.PropertyValues;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.util.Pool;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

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
        GenericObjectPoolConfig poolConfig = parsePoolConfig(configTree);

        if (configTree.getProperty("host") == null || configTree.getProperty("port") == null) {
            throw new CacheException("redis config info host or port must be defined");
        }
        String host = (String) configTree.getProperty("host");
        int port = Integer.parseInt((String) configTree.getProperty("port"));
        int database = Integer.parseInt((String) configTree.getProperty("db", "0"));
        String password = (String) configTree.getProperty("password");
        int timeOut = Integer.parseInt((String) configTree.getProperty("timeOut", "2000"));
        String clientName = (String) configTree.getProperty("clientName", null);
        boolean ssl = Boolean.parseBoolean((String) configTree.getProperty("ssl", "False"));

        String master = (String) configTree.getProperty("master");
        String sentinel = (String) configTree.getProperty("sentinel");
        Pool<Jedis> pool;

        if (sentinel == null) {
            pool = new JedisPool(poolConfig, host, port, timeOut, password, database, clientName, ssl);
        } else {
            String[] strings = sentinel.split(",");
            HashSet<String> sentinelsSet = new HashSet<>();
            for (String s : strings) {
                if (s != null && !s.trim().equals("")) {
                    sentinelsSet.add(s.trim());
                }
            }
            pool = new JedisSentinelPool(master, sentinelsSet, poolConfig, timeOut, password, database, clientName);
        }
        RedisCacheBuilder redisCacheBuilder = new RedisCacheBuilder();
        parseBasicConfigInfo(configTree, redisCacheBuilder);
        redisCacheBuilder.getConfig().setJedisPool(pool);
        redisCacheBuilder.getConfig().setSlaveWeights(null);
        redisCacheBuilder.getConfig().setReadFromSlaves(false);
        redisCacheBuilder.getConfig().setSlaves(null);

        return redisCacheBuilder;
    }

    private GenericObjectPoolConfig parsePoolConfig(CacheConfigTree configTree) {
        try {
            if (ClassUtils.isPresent("org.springframework.boot.context.properties.bind.Binder",
                    this.getClass().getClassLoader())) {
                String prefix = configTree.subTree("poolConfig").getPrefix().toLowerCase();

                Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
                Class<?> bindableClass = Class.forName("org.springframework.boot.context.properties.bind.Bindable");
                Class<?> bindResultClass = Class.forName("org.springframework.boot.context.properties.bind.BindResult");
                Method getMethodOnBinder = binderClass.getMethod("get", Environment.class);
                Method getMethodOnBindResult = bindResultClass.getMethod("get");
                Method bindMethod = binderClass.getMethod("bind", String.class, bindableClass);
                Method ofMethod = bindableClass.getMethod("of", Class.class);
                Object binder = getMethodOnBinder.invoke(null, environment);
                Object bindable = ofMethod.invoke(null, GenericObjectPoolConfig.class);
                Object bindResult = bindMethod.invoke(binder, prefix, bindable);
                return (GenericObjectPoolConfig) getMethodOnBindResult.invoke(bindResult);
            } else {
                // Spring Boot 1.X
                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                Map<String, Object> props = configTree.subTree("poolConfig.").getProperties();

                Class<?> relaxedDataBinderClass = Class.forName("org.springframework.boot.bind.RelaxedDataBinder");
                Class<?> mutablePropertyValuesClass = Class.forName("org.springframework.beans.MutablePropertyValues");
                Constructor<?> c1 = relaxedDataBinderClass.getConstructor(Object.class);
                Constructor<?> c2 = mutablePropertyValuesClass.getConstructor(Map.class);
                Method bindMethod = relaxedDataBinderClass.getMethod("bind", PropertyValues.class);
                Object binder = c1.newInstance(poolConfig);
                bindMethod.invoke(binder, c2.newInstance(props));
                return poolConfig;
            }
        } catch (Throwable ex) {
            throw new CacheException("parse poolConfig fail", ex);
        }

    }
}
