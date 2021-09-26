package com.kp.cache_core.support;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * description: JavaDecoder <br>
 * date: 2021/9/26 6:59 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class JavaDecoder extends AbstractDecoder {

    public static final JavaDecoder JAVA_DECODER_INSTANCE = new JavaDecoder();

    @Override
    public Object decode(byte[] value) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(value);
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }
}
