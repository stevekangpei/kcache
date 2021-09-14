package com.kp.cache_core.external.lettuce;

import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;

/**
 * description: LettuceCacheCode <br>
 * date: 2021/9/14 7:42 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCacheCode implements RedisCodec {

    @Override
    public Object decodeKey(ByteBuffer byteBuffer) {
        return convert(byteBuffer);
    }

    @Override
    public Object decodeValue(ByteBuffer byteBuffer) {
        return convert(byteBuffer);
    }

    @Override
    public ByteBuffer encodeKey(Object o) {
        byte[] bytes = (byte[]) o;
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public ByteBuffer encodeValue(Object o) {
        byte[] bytes = (byte[]) o;
        return ByteBuffer.wrap(bytes);
    }

    private Object convert(ByteBuffer bytes) {
        byte[] bs = new byte[bytes.remaining()];
        bytes.get(bs);
        return bs;
    }

}
