package com.kp.cache_core.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * description: MultiCacheGetResult <br>
 * date: 2021/9/12 7:39 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class MultiCacheGetResult<K, V> extends CacheResult {
    private Map<K, CacheValueHolder> res;

    public MultiCacheGetResult(ResultCode code, String msg) {
        super(code, msg);
    }

    public MultiCacheGetResult(CompletionStage<ResultData> future) {
        super(future);
    }

    public MultiCacheGetResult(Throwable e) {
        super(e);
    }

    @Override
    public void fetchResult(ResultData resultData) {
        super.fetchResult(resultData);
        this.res = (Map<K, CacheValueHolder>) resultData.getData();
    }

    public Map<K, CacheValueHolder> getValue() {
        waitForResult();
        return res;
    }

    public Map<K, V> unWrapValues() {
        if (this.res != null) {
            HashMap<K, V> res = new HashMap<>();
            this.res.forEach((k, v) -> {
                res.put(k, (V) v.getData());
            });
            return res;
        }
        return null;
    }
}
