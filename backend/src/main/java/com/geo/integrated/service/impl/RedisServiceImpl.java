package com.geo.integrated.service.impl;

import com.geo.integrated.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: whtli
 * @date: 2023/02/02
 * @description: redis操作服务层实现
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储数据
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取数据
     *
     * @param key 键
     * @return 根据指定的键获取的值
     */
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置超期时间
     *
     * @param key    键
     * @param expire 过期时间
     * @return 给指定的键设置过期时间
     */
    @Override
    public boolean expire(String key, long expire) {
        return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除数据
     *
     * @param key 键
     */
    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 自增操作
     *
     * @param key   键
     * @param delta 自增步长
     * @return 自增结果
     */
    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }
}
