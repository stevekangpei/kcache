package com.kp.cache_core.external.redis;

import com.kp.cache_core.core.*;
import com.kp.cache_core.exception.CacheException;
import com.kp.cache_core.exception.CacheInvokeException;
import com.kp.cache_core.external.AbstractExternalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.util.Pool;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * description: RedisCache <br>
 * date: 2021/9/12 6:18 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class RedisCache<K, V> extends AbstractExternalCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private RedisCacheConfig<K, V> config;
    private Function<Object, byte[]> encoder;
    private Function<byte[], Object> decoder;

    public RedisCache(RedisCacheConfig<K, V> config) {
        super(config);
        this.config = config;
        this.encoder = config.getEncoder();
        this.decoder = config.getDecoder();

        if (config.isReadFromSlaves()) {
            if (config.getSlaves() == null || config.getSlaves().length == 0) {
                throw new CacheException("slaves pool length 0");
            }
            if (config.getSlaveWeights() == null) {
                config.setSlaveWeights(initDefaultWeights());
            } else if (config.getSlaveWeights().length != config.getSlaves().length) {
                logger.warn("slaves weights length not match slaves length");
                config.setSlaveWeights(initDefaultWeights());
            }
        }
    }

    private Pool<Jedis> getReadableJedisPool() {
        if (!config.isReadFromSlaves()) {
            return config.getJedisPool();
        }
        int[] weights = config.getSlaveWeights();
        int index = randomIndex(weights);
        return config.getSlaves()[index];
    }

    private int randomIndex(int[] weights) {
        int sumOfWeights = 0;
        for (int w : weights) {
            sumOfWeights += w;
        }
        int r = random.nextInt(sumOfWeights);
        int x = 0;
        for (int i = 0; i < weights.length; i++) {
            x += weights[i];
            if (r < x) {
                return i;
            }
        }
        throw new CacheException("assert false");

    }

    private int[] initDefaultWeights() {
        int len = config.getSlaves().length;
        int[] weights = new int[len];
        // 每个权重值设置为100
        Arrays.fill(weights, 100);
        return weights;
    }

    /**
     * put的时候 只是向master 节点put数据
     *
     * @param k
     * @param v
     * @param expireAfterWrite
     * @param timeUnit
     * @return
     */
    @Override
    protected CacheResult do_Put(K k, V v, long expireAfterWrite, TimeUnit timeUnit) {
        try (Jedis jedis = config.getJedisPool().getResource()) {

            CacheValueHolder holder = new CacheValueHolder(v, timeUnit.toMillis(expireAfterWrite));
            byte[] key = buildKey(k);
            String res = jedis.psetex(key, timeUnit.toMillis(expireAfterWrite), encoder.apply(holder));

            if ("OK".equalsIgnoreCase(res)) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else {
                return CacheResult.FAIL_WITHOUT_MSG;
            }
        } catch (Exception e) {
            logger.error("Put k {}, v {} error", k, v);
            throw new CacheInvokeException("Put value into redis cache error", e);
        }
    }

    @Override
    protected CacheResult do_Put_All(Map<K, V> map, long expireAfterWrite, TimeUnit timeUnit) {
        try (Jedis jedis = config.getJedisPool().getResource()) {
            int fails;
            Pipeline pipeline = jedis.pipelined();
            List<Response<String>> responses = new ArrayList<>(map.size());

            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                CacheValueHolder holder = new CacheValueHolder(value,
                        timeUnit.toMillis(expireAfterWrite));
                Response<String> response = pipeline.psetex(buildKey(key), timeUnit.toMillis(expireAfterWrite), encoder.apply(holder));
                responses.add(response);
            }
            pipeline.sync();
            fails = (int) responses.stream().filter(r -> !"OK".equalsIgnoreCase(r.get())).count();

            if (fails == map.size()) {
                return CacheResult.FAIL_WITHOUT_MSG;
            } else if (fails > 0 && fails < map.size()) {
                return CacheResult.PART_SUCCESS_WITHOUT_MSG;
            } else {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            }
        } catch (Exception e) {
            logger.error("Put All error");
            throw new CacheInvokeException("Put all value into redis cache error", e);
        }
    }

    @Override
    protected CacheGetResult<V> do_Get(K k) {
        try (Jedis jedis = getReadableJedisPool().getResource()) {
            byte[] key = buildKey(k);
            byte[] bytes = jedis.get(key);
            long now = System.currentTimeMillis();
            if (bytes != null) {
                CacheValueHolder o = (CacheValueHolder) decoder.apply(bytes);
                if (now > o.getExpireTime()) {
                    return new CacheGetResult<>(ResultCode.EXPIRED, "expired");
                }
                return new CacheGetResult<>(ResultCode.SUCCESS, "success", o);
            }
            return new CacheGetResult<>(ResultCode.NOT_EXIST, "not exist");
        } catch (Exception e) {
            return new CacheGetResult<V>(e);
        }
    }

    @Override
    protected MultiCacheGetResult<K, V> do_Get_All(List<K> keys) {
        try (Jedis jedis = getReadableJedisPool().getResource()) {
            List<K> keysCopy = new ArrayList<>(keys);
            byte[][] bytes = keysCopy.stream().map(this::buildKey).toArray(byte[][]::new);
            Map<K, CacheGetResult<V>> res = new HashMap<>(keys.size());

            List<byte[]> resps = jedis.mget(bytes);
            long now = System.currentTimeMillis();

            if (resps != null) {
                for (int i = 0; i < resps.size(); i++) {
                    byte[] resp = resps.get(i);
                    K k = keys.get(i);
                    if (resp != null) {
                        CacheValueHolder holder = (CacheValueHolder) decoder.apply(resp);
                        if (now > holder.getExpireTime()) {
                            res.put(k, new CacheGetResult<>(ResultCode.EXPIRED, "expired"));
                        } else {
                            res.put(k, new CacheGetResult<>(ResultCode.SUCCESS, "success", holder));
                        }
                    } else {
                        res.put(k, new CacheGetResult<>(ResultCode.NOT_EXIST, "not exist"));
                    }

                }
            }
            return new MultiCacheGetResult<>(ResultCode.SUCCESS, "", res);
        } catch (Exception e) {
            return new MultiCacheGetResult<K, V>(e);
        }
    }

    @Override
    protected CacheResult do_Delete_All(List<K> keys) {
        try (Jedis jedis = config.getJedisPool().getResource()) {
            byte[][] bytes = keys.stream().map(this::buildKey).toArray((len) -> new byte[keys.size()][]);
            Long del = jedis.del(bytes);
            return CacheResult.SUCCESS_WITHOUT_MSG;
        } catch (Exception e) {
            return new CacheResult(e);
        }
    }

    @Override
    protected CacheResult do_Delete(K k) {
        try (Jedis jedis = config.getJedisPool().getResource()) {
            byte[] key = buildKey(k);
            Long del = jedis.del(key);
            if (del == null) {
                return CacheResult.FAIL_WITHOUT_MSG;
            } else if (del == 1) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else if (del == 0) {
                return new CacheResult(ResultCode.NOT_EXIST, null);
            } else {
                return CacheResult.FAIL_WITHOUT_MSG;
            }
        } catch (Exception e) {
            return new CacheResult(e);
        }
    }

    @Override
    public CacheConfig<K, V> config() {
        return this.config;
    }

    @Override
    public void close() throws IOException {

    }
}
