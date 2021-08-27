package top.slomo.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.exception.GlobalException;
import top.slomo.miaosha.result.CodeMsg;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Service
public class RedisService {
    @Autowired
    RedisConfig redisConfig;
    
    @Autowired
    JedisPool jedisPool;



    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            key = prefix.prefix() + key;
            String str = jedis.get(key);
            return stringToBean(str, clazz);
        }
    }


    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String str = beanToString(value);
            key = prefix.prefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, seconds, str);
            }
            return true;
        }
    }


    public Boolean exists(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            key = prefix.prefix() + key;
            return jedis.exists(key);
        }
    }


    public Long incr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            key = prefix.prefix() + key;
            return jedis.incr(key);
        }
    }

    public Long decr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            key = prefix.prefix() + key;
            return jedis.decr(key);
        }
    }

    private <T> String beanToString(T value) {

        return JSON.toJSONString(value);
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }


    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getPoolMaxTotal());
        config.setMaxIdle(redisConfig.getPoolMaxIdle());
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1_000L);

        return new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout() * 1_000);
    }

}
