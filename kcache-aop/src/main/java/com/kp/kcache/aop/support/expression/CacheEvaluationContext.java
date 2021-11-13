package com.kp.kcache.aop.support.expression;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * description: CacheEvaluationContext <br>
 * date: 2021/11/3 7:38 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheEvaluationContext extends MethodBasedEvaluationContext {


    private final Set<String> unavailableVariables = new HashSet<String>(1);


    CacheEvaluationContext(Object rootObject, Method method, Object[] arguments,
                           ParameterNameDiscoverer parameterNameDiscoverer) {

        super(rootObject, method, arguments, parameterNameDiscoverer);
    }

    public void addUnavailableVariable(String name) {
        this.unavailableVariables.add(name);
    }


    /**
     * Load the param information only when needed.
     */
    @Override
    public Object lookupVariable(String name) {
        if (this.unavailableVariables.contains(name)) {
            throw new RuntimeException(name);
        }
        return super.lookupVariable(name);
    }
}
