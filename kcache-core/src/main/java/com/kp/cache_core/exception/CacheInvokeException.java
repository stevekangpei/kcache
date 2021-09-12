package com.kp.cache_core.exception;

/**
 * description: CacheInvokeException <br>
 * date: 2021/9/12 4:26 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheInvokeException extends CacheException {

    private static final long serialVersionUID = 6463807185643539816L;

    public CacheInvokeException(String message) {
        super(message);
    }

    public CacheInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheInvokeException(Throwable cause) {
        super(cause);
    }

    public CacheInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
