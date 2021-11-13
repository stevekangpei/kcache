package com.kp.kcache.autoconfigure.condition;

import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description: AbstractCacheCondition <br>
 * date: 2021/9/15 10:43 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheCondition implements Condition {

    protected String[] cacheNames;

    public AbstractCacheCondition(String... cacheNames) {
        Objects.requireNonNull(cacheNames, "cacheNames should not be null");
        this.cacheNames = cacheNames;
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        ConfigurableEnvironment environment = (ConfigurableEnvironment) conditionContext.getEnvironment();
        CacheConfigTree cacheConfigTree = new CacheConfigTree(environment, "kcache.");
        return match(cacheConfigTree, "local.") || match(cacheConfigTree, "remote.");
    }

    private boolean match(CacheConfigTree configTree, String prefix) {
        CacheConfigTree subTree = configTree.subTree(prefix);
        /**
         * 需要获取所有的 type,
         */
        Map<String, Object> properties = subTree.getProperties();
        Set<String> cacheAreas = properties.keySet().stream().map(key -> key.substring(0, key.indexOf("."))).collect(Collectors.toSet());
        Set<String> caches = new HashSet<>(Arrays.asList(cacheNames));
        long count = cacheAreas.stream().map(s -> s + ".type").map(s -> (String) properties.get(s)).filter(caches::contains).count();
        return count > 0;
    }
}
