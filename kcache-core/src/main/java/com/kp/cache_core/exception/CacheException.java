package com.kp.cache_core.exception;

/**
 * description: CacheException <br>
 * date: 2021/9/12 4:23 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -1026188776184682712L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
