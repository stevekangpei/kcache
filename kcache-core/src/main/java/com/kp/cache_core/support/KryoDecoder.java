package com.kp.cache_core.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import java.io.ByteArrayInputStream;

/**
 * description: KryoDecoder <br>
 * date: 2021/9/26 7:00 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class KryoDecoder extends AbstractDecoder {

    public static final KryoDecoder KRYO_DECODER = new KryoDecoder();

    static ThreadLocal<Kryo> kryoDecoderThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    });

    @Override
    public Object decode(byte[] value) {
        ByteArrayInputStream bis = new ByteArrayInputStream(value);
        Input input = new Input(bis);

        Kryo kryo = kryoDecoderThreadLocal.get();
        kryo.setClassLoader(this.getClass().getClassLoader());
        return kryo.readClassAndObject(input);
    }

    @Override
    public Object apply(byte[] bytes) {
        return decode(bytes);
    }
}
