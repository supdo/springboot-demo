package com.supdo.sb.demo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.supdo.sb.demo.plugin.shiro.MyShiroRealm;
import com.supdo.sb.demo.plugin.shiro.RedisCacheManager;
import com.supdo.sb.demo.plugin.shiro.RedisManager;
import com.supdo.sb.demo.plugin.shiro.RedisSessionDAO;

@Configuration
public class ShiroConfig {
	
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		
		// 权限控制map.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		//filterChainDefinitionMap.put("/user", "perms[add1]");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		
		return shiroFilterFactoryBean;
	}
	
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myShiroRealm());
		// 自定义缓存实现 使用redis
		//RedisCacheManager cache = cacheManager();
		securityManager.setCacheManager(cacheManager());
		// 自定义session管理 使用redis
		securityManager.setSessionManager(SessionManager());
		return securityManager;
	}
	
	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		return myShiroRealm;
	}
	
	/**
	 * 配置shiro redisManager
	 * 
	 * @return
	 */
	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		return redisManager;
	}
	
	/**
	 * cacheManager 缓存 redis实现
	 * 
	 * @return
	 */
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}
	
	
	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
	 */
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}
	/**
	 * shiro session的管理
	 */
	public DefaultWebSessionManager SessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO());
		return sessionManager;
	}
	
	
	//开启注解模式
	@Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
            = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
