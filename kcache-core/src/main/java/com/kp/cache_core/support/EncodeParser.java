package com.kp.cache_core.support;

import java.util.function.Function;

/**
 * description: EncodeParser <br>
 * date: 2021/11/13 6:48 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface EncodeParser {

    Function<Object, byte[]> parseEncoder(String valueEncoder);
    Function<byte[], Object> parseDecoder(String valueDecoder);

}
