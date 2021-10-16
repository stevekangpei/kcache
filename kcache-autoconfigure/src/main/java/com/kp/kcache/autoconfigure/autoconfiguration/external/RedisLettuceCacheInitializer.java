package com.kp.kcache.autoconfigure.autoconfiguration.external;

import com.kp.cache_core.cache_builder.ICacheBuilder;
import com.kp.cache_core.cache_builder.external.RedisLettuceBuilder;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.external.lettuce.LettuceCacheCode;
import com.kp.kcache.autoconfigure.condition.LettuceCacheCondition;
import com.kp.kcache.autoconfigure.support.CacheConfigTree;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description: RedisLettuceCacheInitializer <br>
 * date: 2021/9/23 7:54 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
@Conditional(LettuceCacheCondition.class)
public class RedisLettuceCacheInitializer extends AbstractExternalCacheInitializer {

    public RedisLettuceCacheInitializer() {
        super("lettuce");
    }

    @Override
    protected ICacheBuilder buildCache(CacheConfigTree configTree) {
        Map<String, Object> map = configTree.subTree("uri").getProperties();
        // 数据节点偏好设置
        String readFromStr = (String) configTree.getProperty("readFrom");
        // 集群模式
        String mode = (String) configTree.getProperty("mode");
        // 异步获取结果的超时时间，默认1s
        long asyncResultTimeoutInMillis = Long.parseLong(
                (String) configTree.getProperty("asyncResultTimeoutInMillis"));
        ReadFrom readFrom = null;

        if (readFromStr != null) {
            /*
             * MASTER：只从Master节点中读取。
             * MASTER_PREFERRED：优先从Master节点中读取。
             * SLAVE_PREFERRED：优先从Slave节点中读取。
             * SLAVE：只从Slave节点中读取。
             * NEAREST：使用最近一次连接的Redis实例读取。
             */
            readFrom = ReadFrom.valueOf(readFromStr.trim());
        }
        AbstractRedisClient client;
        StatefulConnection connection = null;
        if (map == null || map.size() == 0) {
            throw new CacheException("lettuce uri is required");
        } else {
            List<RedisURI> uriList = map.values().stream().map((k) -> RedisURI.create(URI.create(k.toString())))
                    .collect(Collectors.toList());
            if (uriList.size() == 1) {
                RedisURI uri = uriList.get(0);
                if (readFrom == null) {
                    // 创建一个 Redis 客户端
                    client = RedisClient.create(uri);
                    // 设置失去连接时的行为，拒绝命令，默认为 DEFAULT
                    ((RedisClient) client).setOptions(ClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                } else {
                    // 创建一个 Redis 客户端
                    client = RedisClient.create();
                    ((RedisClient) client).setOptions(ClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                    // 创建一个安全连接并设置数据节点偏好
                    StatefulRedisMasterSlaveConnection c = MasterSlave.connect(
                            (RedisClient) client, new LettuceCacheCode(), uri);
                    c.setReadFrom(readFrom);
                    connection = c;
                }
            } else {
                if (mode != null && mode.equalsIgnoreCase("MasterSlave")) {
                    client = RedisClient.create();
                    ((RedisClient) client).setOptions(ClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                    StatefulRedisMasterSlaveConnection c = MasterSlave.connect(
                            (RedisClient) client, new LettuceCacheCode(), uriList);
                    if (readFrom != null) {
                        c.setReadFrom(readFrom);
                    }
                    connection = c;
                } else {
                    // 创建一个 Redis 客户端
                    client = RedisClusterClient.create(uriList);
                    ((RedisClusterClient) client).setOptions(ClusterClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                    if (readFrom != null) {
                        StatefulRedisClusterConnection c = ((RedisClusterClient) client).connect(new LettuceCacheCode());
                        c.setReadFrom(readFrom);
                        connection = c;
                    }
                }
            }
        }

        RedisLettuceBuilder redisLettuceBuilder = new RedisLettuceBuilder();
        parseBasicConfigInfo(configTree, redisLettuceBuilder);
        redisLettuceBuilder.getConfig().setConnection(connection);
        redisLettuceBuilder.getConfig().setClient(client);
        redisLettuceBuilder.getConfig().setAsyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
        return redisLettuceBuilder;
    }
}
