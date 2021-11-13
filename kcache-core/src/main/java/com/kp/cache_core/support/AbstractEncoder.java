package com.kp.cache_core.support;

import java.util.function.Function;

/**
 * description: AbstractEncoder <br>
 * date: 2021/9/26 6:56 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractEncoder implements Function<Object, byte[]> {

    public abstract byte[] encode(Object object) throws Exception;
}
