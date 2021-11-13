package com.kp.kcache.annos;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * description: AllowPenertation <br>
 * date: 2021/11/1 7:23 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowPenetration {

    int timeOut() default 0;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
