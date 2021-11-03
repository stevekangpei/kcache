package com.kp.kcache.aop.support;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * description: SimpleKey <br>
 * date: 2021/11/3 7:24 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class SimpleKey implements Serializable {

    public static final Object EMPTY = new Object();
    private final Object[] params;
    private final int hashCode;

    public SimpleKey(Object[] params) {
        this.params = new Object[params.length];
        System.arraycopy(params, 0, this.params, 0, params.length);
        this.hashCode = Arrays.deepHashCode(this.params);
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || (obj instanceof SimpleKey
                && Arrays.deepEquals(this.params, ((SimpleKey) obj).params)));
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }

    public Object[] getParams() {
        return params;
    }

}
