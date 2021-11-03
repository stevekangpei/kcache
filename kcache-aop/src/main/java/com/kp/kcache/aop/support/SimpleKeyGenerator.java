package com.kp.kcache.aop.support;

import com.kp.kcache.aop.support.expression.CacheOperationExpressionEvaluator;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * description: SimpleKeyGenerator <br>
 * date: 2021/11/3 7:24 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
public class SimpleKeyGenerator implements KeyGenerator {
    /**
     * SpEL表达式计算器
     */
    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    @Override
    public Object generate(String keySpEL, Object target, Method method, Object... params) {
        if (StringUtils.isEmpty(keySpEL)) {
            if (params.length == 0) {
                return SimpleKey.EMPTY;
            }
            if (params.length == 1) {
                Object param = params[0];
                if (param != null && !param.getClass().isArray()) {
                    return param;
                }
            }
            return new SimpleKey(params);
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, params, target,
                targetClass, CacheOperationExpressionEvaluator.NO_RESULT);
        AnnotatedElementKey methodCacheKey = new AnnotatedElementKey(method, targetClass);
        Object keyValue = evaluator.key(keySpEL, methodCacheKey, evaluationContext);
        return Objects.isNull(keyValue) ? "null" : keyValue;
    }
}
