package com.kp.kcache.annos;

import com.kp.kcache.autoconfigure.KCacheAutoconfiguraton;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * description: EnableCacheKCache <br>
 * date: 2021/11/1 7:39 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({KCacheAutoconfiguraton.class})
public @interface EnableCreateKCache {
}
