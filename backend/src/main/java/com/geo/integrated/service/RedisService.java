package com.geo.integrated.service;

/**
 * @author: whtli
 * @date: 2023/02/02
 * @description: redis操作服务层
 * 对象和数组都以json形式进行存储
 */
public interface RedisService {
    /**
     * 存储数据
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, String value);

    /**
     * 获取数据
     *
     * @param key 键
     * @return 根据指定的键获取的值
     */
    String get(String key);

    /**
     * 设置超期时间
     *
     * @param key    键
     * @param expire 过期时间
     * @return 给指定的键设置过期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 自增操作
     *
     * @param key   键
     * @param delta 自增步长
     * @return 自增结果
     */
    Long increment(String key, long delta);
}
