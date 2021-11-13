package com.kp.cache_core.support;

import com.kp.cache_core.exception.CacheException;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * description: JavaEncoder <br>
 * date: 2021/9/26 6:58 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class JavaEncoder extends AbstractEncoder {

    public static final JavaEncoder JAVA_ENCODER_INSTANCE = new JavaEncoder();

    @Override
    public byte[] encode(Object object) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        return bos.toByteArray();
    }

    @Override
    public byte[] apply(Object o) {
        try {
            return encode(o);
        } catch (Exception e) {
            throw new CacheException("error encoding object, " + o.toString(), e);
        }
    }
}
