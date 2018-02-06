package com.supdo.sb.demo.plugin.shiro;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SuppressWarnings("unchecked")
public class RedisManager {

	@Value("${spring.redis.timeout}")
	private int expire = 0;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	
	private ValueOperations<String, byte[]> operations;
	
	public RedisManager(){
		
	}
	
	/**
	 * 初始化方法
	 */
	
	public void init(){
		this.operations = redisTemplate.opsForValue();
	}
	
	/**
	 * get value from redis
	 * @param key
	 * @return
	 */
	public byte[] get(String key){
		byte[] value = null;
		value = this.operations.get(key);
		return value;
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(String key,byte[] value){
		this.operations.set(key, value);
		return value;
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public byte[] set(String key,byte[] value,int expire){
		this.operations.set(key, value, expire, TimeUnit.SECONDS);
		return value;
	}
	
	/**
	 * del
	 * @param key
	 */
	public void del(String key){
		this.redisTemplate.delete(key);
	}
	
	/**
	 * flush
	 */
	@SuppressWarnings("rawtypes")
	public String flushDB(){
		return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
	}
	
	/**
	 * size
	 */
	@SuppressWarnings("rawtypes")
	public Long dbSize(){
		return (Long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
	}

	/**
	 * keys
	 * @param regex
	 * @return
	 */
	public Set<byte[]> keys(String pattern){
		Set<byte[]> keys = null;
		keys = this.redisTemplate.keys(pattern.getBytes());
		return keys;
	}
	
	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}
	
}
