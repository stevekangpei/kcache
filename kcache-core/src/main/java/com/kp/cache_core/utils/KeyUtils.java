package com.kp.cache_core.utils;

import com.kp.cache_core.exception.CacheException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: KeyUtils <br>
 * date: 2021/9/12 6:16 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public final class KeyUtils {

    public static byte[] buildKeyAfterConvert(Object newKey, String prefix) throws IOException {
        if (newKey == null) {
            throw new NullPointerException("key can't be null");
        }
        byte[] keyBytesWithOutPrefix = null;
        // 将key转换成byte[]类型
        if (newKey instanceof String) {
            keyBytesWithOutPrefix = newKey.toString().getBytes("UTF-8");
        } else if (newKey instanceof byte[]) {
            keyBytesWithOutPrefix = (byte[]) newKey;
        } else if (newKey instanceof Number) {
            keyBytesWithOutPrefix = (newKey.getClass().getSimpleName() + newKey).getBytes("UTF-8");
        } else if (newKey instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss,SSS");
            keyBytesWithOutPrefix = (newKey.getClass().getSimpleName() + sdf.format(newKey)).getBytes();
        } else if (newKey instanceof Boolean) {
            keyBytesWithOutPrefix = newKey.toString().getBytes("UTF-8");
        } else if (newKey instanceof Serializable) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(newKey);
            os.close();
            bos.close();
            keyBytesWithOutPrefix = bos.toByteArray();
        } else {
            throw new CacheException("can't convert key of class: " + newKey.getClass());
        }
        byte[] prefixBytes = prefix.getBytes("UTF-8");
        byte[] rt = new byte[prefixBytes.length + keyBytesWithOutPrefix.length];
        System.arraycopy(prefixBytes, 0, rt, 0, prefixBytes.length);
        System.arraycopy(keyBytesWithOutPrefix, 0, rt, prefixBytes.length, keyBytesWithOutPrefix.length);
        return rt;
    }
}
