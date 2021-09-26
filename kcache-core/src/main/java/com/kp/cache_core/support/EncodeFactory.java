package com.kp.cache_core.support;

import java.util.HashMap;
import java.util.Map;

import static com.kp.cache_core.support.JavaEncoder.JAVA_ENCODER_INSTANCE;
import static com.kp.cache_core.support.KryoEncoder.KRYO_ENCODER_INSTANCE;

/**
 * description: EncodeFactory <br>
 * date: 2021/9/26 7:00 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public final class EncodeFactory {


    public static final Map<String, AbstractEncoder> ENCODERS = new HashMap<>();

    static {
        ENCODERS.put("java", JAVA_ENCODER_INSTANCE);
        ENCODERS.put("kryo", KRYO_ENCODER_INSTANCE);
    }
}
