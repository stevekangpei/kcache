package com.kp.kcache.aop.support;

import java.lang.reflect.Method;

/**
 * description: KeyGenerator <br>
 * date: 2021/11/3 7:23 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface KeyGenerator {


    Object generate(String keySpEL, Object target, Method method, Object... params);

}
