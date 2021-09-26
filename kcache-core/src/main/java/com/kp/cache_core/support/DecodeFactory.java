package com.kp.cache_core.support;

import java.util.HashMap;
import java.util.Map;

import static com.kp.cache_core.support.JavaDecoder.JAVA_DECODER_INSTANCE;
import static com.kp.cache_core.support.KryoDecoder.KRYO_DECODER;

/**
 * description: DecodeFactory <br>
 * date: 2021/9/26 7:01 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public final class DecodeFactory {

    public static final Map<String, AbstractDecoder> DECODERS = new HashMap<>();

    static {
        DECODERS.put("java", JAVA_DECODER_INSTANCE);
        DECODERS.put("kryo", KRYO_DECODER);
    }
}
