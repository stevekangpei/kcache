package com.kp.cache_core.support;

import com.kp.cache_core.exception.CacheException;

import java.util.function.Function;

/**
 * description: DefaultEncodeParser <br>
 * date: 2021/11/13 6:48 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class DefaultEncodeParser implements EncodeParser {

    @Override
    public Function<Object, byte[]> parseEncoder(String valueEncoder) {
        AbstractEncoder encoder = EncodeFactory.getEncoderByKey(valueEncoder);
        if (encoder == null) {
            throw new CacheException("invalid encoder key");
        }
        return encoder;
    }

    @Override
    public Function<byte[], Object> parseDecoder(String valueDecoder) {
        AbstractDecoder decoder = DecodeFactory.getDecoderByKey(valueDecoder);
        if (decoder == null) {
            throw new CacheException("invalid decoder key");
        }
        return decoder;
    }
}
