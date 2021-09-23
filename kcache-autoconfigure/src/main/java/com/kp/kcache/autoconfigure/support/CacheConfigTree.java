package com.kp.kcache.autoconfigure.support;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * description: CacheConfigTree <br>
 * date: 2021/9/23 7:22 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheConfigTree {

    private ConfigurableEnvironment environment;
    private String prefix;

    public CacheConfigTree(ConfigurableEnvironment environment, String prefix) {
        this.environment = environment;
        this.prefix = prefix;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> res = new HashMap<>();

        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                for (String propertyName : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                    if (propertyName != null && propertyName.startsWith(prefix)) {
                        String key = propertyName.substring(prefix.length());
                        res.put(key, environment.getProperty(propertyName));
                    }
                }
            }
        }
        return res;
    }

    public String fullPrefixOfKey(String prefix) {
        return this.prefix + prefix;
    }

    public CacheConfigTree subTree(String prefix) {
        return new CacheConfigTree(environment, fullPrefixOfKey(prefix));
    }

    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
