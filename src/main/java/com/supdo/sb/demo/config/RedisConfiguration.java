package com.supdo.sb.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }
	
	
	/**
     * 自定义生成key的规则
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                //格式化缓存key字符串
                StringBuilder sb = new StringBuilder();
                //追加类名
                sb.append(o.getClass().getName());
                //追加方法名
                sb.append(":"+method.getName()+":");
                //遍历参数并且追加
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println("调用Redis缓存Key : " + sb.toString());
                return sb.toString();
            }
        };
    }
    /**
     * 采用RedisCacheManager作为缓存管理器
     * @param redisTemplate
     * @return
     */
    @SuppressWarnings("rawtypes")
	@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }
}
