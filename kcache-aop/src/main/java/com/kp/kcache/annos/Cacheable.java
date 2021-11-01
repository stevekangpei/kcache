package com.kp.kcache.annos;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import static com.kp.kcache.annos.Constants.DEFAULT;
import static com.kp.kcache.annos.Constants.LOCAL_LIMIT;

/**
 * description: Cacheable <br>
 * date: 2021/11/1 7:22 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 * <p>
 * (1) area  cache 区域
 * (2) name  cache 实例名称
 * (3) timeUnit
 * (4) expire
 * (5) localExpire
 * (6) CacheType
 * (7) key cache key
 * (8) condition
 * (9) cacheNullValues
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {

    /**
     * cache 区域
     *
     * @return
     */
    String area() default DEFAULT;

    /**
     * cache 实例的名称
     *
     * @return
     */
    String name() default "";

    /**
     * 时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 过期时间
     *
     * @return
     */
    int expire() default 0;

    /**
     * 本地过期时间
     *
     * @return
     */
    int localExpire() default 0;

    CacheType cacheType() default CacheType.BOTH;

    /**
     * SpEL 表达式，用于生成key
     *
     * @return
     */
    String key() default "";

    /**
     * 是否Cache null 值
     *
     * @return
     */
    boolean cacheNullValues() default false;

    /**
     * cache 条件
     *
     * @return
     */
    String condition() default "";

    int localLimit() default LOCAL_LIMIT;
}
