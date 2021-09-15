package com.kp.kcache.autoconfigure.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

/**
 * description: AbstractCacheCondition <br>
 * date: 2021/9/15 10:43 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheCondition implements Condition {

    protected String[] cacheNames;

    public AbstractCacheCondition(String[] cacheNames) {
        Objects.requireNonNull(cacheNames, "cacheNames should not be null");
        this.cacheNames = cacheNames;
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return false;
    }
}
