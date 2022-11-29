package com.brecycle.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis通用工具类
 *
 * @author cmgun
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 是否存在指定的key
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 自增
     * @param key
     * @return
     */
    public Long incr(String key) {
        ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
        return operation.increment(key);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public ValueOperations<Object, Object> setCacheObject(Object key, Object value) {
        ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     * @return 缓存的对象
     */
    public ValueOperations<Object, Object> setCacheObject(Object key, Object value, Integer timeout, TimeUnit timeUnit) {
        ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value, timeout, timeUnit);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public Object getCacheObject(Object key) {
        ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public void deleteObject(Object key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection
     */
    public void deleteObject(Collection collection) {
        redisTemplate.delete(collection);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public void expire(String key, int expire, TimeUnit timeUnit) {
        redisTemplate.expire(key, expire, timeUnit);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public ListOperations<Object, Object> setCacheList(Object key, List<Object> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (Object o : dataList) {
                listOperation.leftPush(key, o);
            }
        }
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public List<Object> getCacheList(String key) {
        List<Object> dataList = new ArrayList<>();
        ListOperations<Object, Object> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);
        if (null != size) {
            for (int i = 0; i < size; i++) {
                dataList.add(listOperation.index(key, i));
            }
        }
        return dataList;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public BoundSetOperations<Object, Object> setCacheSet(String key, Set<Object> dataSet) {
        BoundSetOperations<Object, Object> setOperation = redisTemplate.boundSetOps(key);
        for (Object o : dataSet) {
            setOperation.add(o);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public Set<Object> getCacheSet(Object key) {
        Set<Object> dataSet = new HashSet<>();
        BoundSetOperations<Object, Object> operation = redisTemplate.boundSetOps(key);
        dataSet = operation.members();
        return dataSet;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public HashOperations<Object, Object, Object> setCacheMap(Object key, Map<Object, Object> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {
            for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public Map<Object, Object> getCacheMap(Object key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<Object> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
