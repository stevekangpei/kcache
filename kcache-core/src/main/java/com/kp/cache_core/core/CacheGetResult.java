package com.kp.cache_core.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * description: CacheGetResult <br>
 * date: 2021/9/12 7:39 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class CacheGetResult<V> extends CacheResult {

    private V v;
    private CacheValueHolder holder;

    public CacheGetResult(ResultCode code, String msg) {
        super(code, msg);
    }

    public CacheGetResult(ResultCode code, String msg, CacheValueHolder holder) {
        super(CompletableFuture.completedFuture(new ResultData(code, msg, holder)));
    }

    public CacheGetResult(CompletionStage<ResultData> future) {
        super(future);
    }

    public CacheGetResult(Throwable e) {
        super(e);
    }

    @Override
    public void fetchResult(ResultData resultData) {
        super.fetchResult(resultData);
        holder = (CacheValueHolder) resultData.getData();
        this.v = (V) unWrapValue(holder);
    }

    public V getData() {
        if (v != null) return v;
        waitForResult();
        return this.v;
    }

    private Object unWrapValue(CacheValueHolder holder) {
        Object v = holder;
        while (v instanceof CacheValueHolder) {
            v = ((CacheValueHolder) v).getData();
        }
        return v;
    }
}
