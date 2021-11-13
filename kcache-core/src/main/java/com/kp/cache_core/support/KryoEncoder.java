package com.kp.cache_core.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import java.lang.ref.WeakReference;

/**
 * description: KryoEncoder <br>
 * date: 2021/9/26 6:59 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class KryoEncoder extends AbstractEncoder {

    public static final KryoEncoder KRYO_ENCODER_INSTANCE = new KryoEncoder();

    static ThreadLocal<Object[]> kryoEncoderThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        byte[] buffer = new byte[512];
        WeakReference<byte[]> ref = new WeakReference<>(buffer);
        return new Object[]{kryo, ref};
    });

    @Override
    public byte[] encode(Object object) {
        Object[] kryoAndBuffer = kryoEncoderThreadLocal.get();
        Kryo kryo = (Kryo) kryoAndBuffer[0];
        WeakReference<byte[]> bufferRefer = (WeakReference<byte[]>) kryoAndBuffer[1];

        byte[] buffer = bufferRefer.get();
        if (buffer == null) {
            buffer = new byte[512];
        }
        Output output = new Output(buffer, -1);
        kryo.writeClassAndObject(output, object);
        return output.toBytes();
    }

    @Override
    public byte[] apply(Object o) {
        return encode(o);
    }
}
