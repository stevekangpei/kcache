package com.kp.kcache.aop.support;

import org.springframework.asm.Type;

import java.lang.reflect.Method;

/**
 * description: AopUtils <br>
 * date: 2021/11/1 10:33 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class AopUtils {


    public static String getKey(Method method, Class clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append('.');
        sb.append(method.getName());
        sb.append(Type.getMethodDescriptor(method));
        if (clazz != null) {
            sb.append('_');
            sb.append(clazz.getName());
        }
        return sb.toString();
    }
}
