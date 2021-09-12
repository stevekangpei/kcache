package com.kp.cache_core.external.lettuce;

import com.kp.cache_core.external.ExternalCacheConfig;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;

import java.time.Duration;

/**
 * description: LettuceCacheConfig <br>
 * date: 2021/9/12 6:45 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class LettuceCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    private AbstractRedisClient client;
    private StatefulConnection connection;
    /**
     * 异步超时时间
     */
    private long asyncResultTimeoutInMillis = Duration.ofMillis(1000).toMillis();

    public AbstractRedisClient getClient() {
        return client;
    }

    public void setClient(AbstractRedisClient client) {
        this.client = client;
    }

    public StatefulConnection getConnection() {
        return connection;
    }

    public void setConnection(StatefulConnection connection) {
        this.connection = connection;
    }

    public long getAsyncResultTimeoutInMillis() {
        return asyncResultTimeoutInMillis;
    }

    public void setAsyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        this.asyncResultTimeoutInMillis = asyncResultTimeoutInMillis;
    }
}
