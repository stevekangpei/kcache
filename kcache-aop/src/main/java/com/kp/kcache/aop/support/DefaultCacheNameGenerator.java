package com.kp.kcache.aop.support;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: DefaultCacheNameGenerator <br>
 * date: 2021/11/3 10:03 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class DefaultCacheNameGenerator {
    protected static final ConcurrentHashMap<Method, String> cacheNameMap = new ConcurrentHashMap<>();

    public static String generateCacheName(Method method, Object targetObject) {

        String cacheName = cacheNameMap.get(method);

        if (cacheName == null) {
            final StringBuilder sb = new StringBuilder();

            String className = method.getDeclaringClass().getName();
            // 追加类名（如果包含需要隐藏的包名则去除）
            sb.append('.');
            // 追加方法名
            sb.append(method.getName());
            sb.append('(');
            /*
             * 依次追加方法参数类型名称
             */
            for(Class<?> c : method.getParameterTypes()){
                getDescriptor(sb, c);
            }
            sb.append(')');

            String str = sb.toString();
            cacheNameMap.put(method, str);
            return str;
        }

        return cacheName;
    }

    protected static void getDescriptor(final StringBuilder sb, final Class<?> c) {
        Class<?> d = c;
        while (true) {
            if (d.isPrimitive()) {
                char car;
                if (d == Integer.TYPE) {
                    car = 'I';
                } else if (d == Void.TYPE) {
                    car = 'V';
                } else if (d == Boolean.TYPE) {
                    car = 'Z';
                } else if (d == Byte.TYPE) {
                    car = 'B';
                } else if (d == Character.TYPE) {
                    car = 'C';
                } else if (d == Short.TYPE) {
                    car = 'S';
                } else if (d == Double.TYPE) {
                    car = 'D';
                } else if (d == Float.TYPE) {
                    car = 'F';
                } else /* if (d == Long.TYPE) */{
                    car = 'J';
                }
                sb.append(car);
                return;
            } else if (d.isArray()) {
                sb.append('[');
                d = d.getComponentType();
            } else {
                sb.append('L');
                String name = d.getName();
                name = ClassUtil.getShortClassName(name);
                sb.append(name);
                sb.append(';');
                return;
            }
        }
    }
}
