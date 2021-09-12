package com.kp.cache_core.external.redis;

import com.kp.cache_core.external.ExternalCacheConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * description: RedisCacheConfig <br>
 * date: 2021/9/12 6:45 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class RedisCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    /**
     * Redis 主节点的连接池
     */
    private Pool<Jedis> jedisPool;
    /**
     * redis 从节点的连接池
     */
    private Pool<Jedis>[] slaves;

    /**
     * 是否只从从节点读取数据
     */
    private boolean readFromSlaves;

    private int[] slaveWeights;

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Pool<Jedis>[] getSlaves() {
        return slaves;
    }

    public void setSlaves(Pool<Jedis>[] slaves) {
        this.slaves = slaves;
    }

    public boolean isReadFromSlaves() {
        return readFromSlaves;
    }

    public void setReadFromSlaves(boolean readFromSlaves) {
        this.readFromSlaves = readFromSlaves;
    }

    public int[] getSlaveWeights() {
        return slaveWeights;
    }

    public void setSlaveWeights(int[] slaveWeights) {
        this.slaveWeights = slaveWeights;
    }
}
