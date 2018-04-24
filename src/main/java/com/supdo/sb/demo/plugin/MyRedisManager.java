package com.supdo.sb.demo.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Component
public class MyRedisManager {

    @Value("${spring.redis.timeout}")
    private int expire = 60*60;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> operations;

    public MyRedisManager(){

    }

    /**
     * 初始化方法
     */

    public void init(){
        this.operations = redisTemplate.opsForValue();
    }


    public Object get(String key){
        Object value = null;
        value = operations.get(key);
        return value;
    }

    public Object set(String key, Object value){
        operations.set(key, value, expire, TimeUnit.SECONDS);
        return value;
    }

    public Object set(String key, Object value, int expire){
        operations.set(key, value, expire, TimeUnit.SECONDS);
        return value;
    }

    public void del(String key){
        redisTemplate.delete(key);
    }
}
