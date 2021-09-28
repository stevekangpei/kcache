package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.exception.CacheException;
import com.kp.kcache.autoconfigure.condition.RedisCacheCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.PropertyValues;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

        String host = (String) configTree.getProperty("host");
        int port = Integer.parseInt((String) configTree.getProperty("port"));
        int database = Integer.parseInt((String) configTree.getProperty("db"));
        String password = (String) configTree.getProperty("password");
        return null;
    }

    private GenericObjectPoolConfig parsePoolConfig(CacheConfigTree configTree) {
        try {
            // Spring Boot 2.0 removed RelaxedDataBinder class. Binder class not exists in 1.X
            if (ClassUtils.isPresent("org.springframework.boot.context.properties.bind.Binder",
                    this.getClass().getClassLoader())) {
                // Spring Boot 2.0+
                String prefix = configTree.subTree("poolConfig").getPrefix().toLowerCase();

                // invoke following code by reflect
                // Binder binder = Binder.get(environment);
                // return binder.bind(name, Bindable.of(GenericObjectPoolConfig.class)).get();

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

                // invoke following code by reflect
                //RelaxedDataBinder binder = new RelaxedDataBinder(poolConfig);
                //binder.bind(new MutablePropertyValues(props));

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
