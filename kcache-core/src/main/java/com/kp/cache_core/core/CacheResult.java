package com.kp.cache_core.core;

import com.kp.cache_core.exception.CacheException;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * 用于封装Cache的数据
 * description: CacheResult <br>
 * date: 2021/7/25 10:15 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheResult {
    public static final String MSG_ILLEGAL_ARGUMENT = "illegal argument";
    private ResultCode code;
    private String msg;
    private CompletionStage<ResultData> future;

    public static final CacheResult SUCCESS_WITHOUT_MSG = new CacheResult(ResultCode.SUCCESS, null);
    public static final CacheResult PART_SUCCESS_WITHOUT_MSG = new CacheResult(ResultCode.PART_SUCCESS, null);
    public static final CacheResult FAIL_WITHOUT_MSG = new CacheResult(ResultCode.FAIL, "");
    public static final CacheResult FAIL_WITH_ILLEGAL_ARGUMENT = new CacheResult(ResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);


    public CacheResult(ResultCode code, String msg) {
        this(CompletableFuture.completedFuture(new ResultData(code, msg, null)));
    }

    public CacheResult(CompletionStage<ResultData> future) {
        this.future = future;
    }

    public CacheResult(Throwable e) {
        this.future = CompletableFuture.completedFuture(new ResultData(e));
    }


    public void waitForResult(Duration timeOut) {
        try {
            ResultData resultData = this.future.toCompletableFuture().get(timeOut.toMillis(), TimeUnit.MILLISECONDS);
            fetchResult(resultData);
        } catch (Exception e) {
            throw new CacheException("get error", e);
        }
    }

    public void fetchResult(ResultData resultData) {
        this.msg = resultData.getMsg();
        this.code = resultData.getResultCode();
    }

    public void waitForResult() {
        waitForResult(Duration.ofMillis(100));
    }

    public boolean isSuccess() {
        return getCode() == ResultCode.SUCCESS;
    }

    public ResultCode getCode() {
        waitForResult();
        return this.code;
    }
}
