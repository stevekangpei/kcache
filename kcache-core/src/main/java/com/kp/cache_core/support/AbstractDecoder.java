package com.kp.cache_core.support;

import java.util.function.Function;

/**
 * description: AbstractDecoder <br>
 * date: 2021/9/26 6:55 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractDecoder implements Function<byte[], Object> {

    public abstract Object decode(byte[] value) throws Exception;
}
