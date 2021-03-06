package com.kp.kcache.annos;

import java.lang.annotation.*;

import static com.kp.kcache.annos.Constants.DEFAULT;

/**
 * description: CacheUpdate <br>
 * date: 2021/11/1 7:22 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheUpdate {

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
     * SpEL 表达式，用于生成key
     *
     * @return
     */
    String key() default "";

    CacheType cacheType() default CacheType.BOTH;

    boolean multiKeys() default false;
}
