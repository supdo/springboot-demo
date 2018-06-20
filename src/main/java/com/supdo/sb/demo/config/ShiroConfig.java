package com.supdo.sb.demo.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Clients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.pac4j.core.config.Config;

import com.supdo.sb.demo.plugin.shiro.MyShiroRealm;
import com.supdo.sb.demo.plugin.shiro.RedisCacheManager;
import com.supdo.sb.demo.plugin.shiro.RedisManager;
import com.supdo.sb.demo.plugin.shiro.RedisSessionDAO;

import javax.servlet.Filter;

/**
 * shrio-spring的配置，现在改为shiro-spring-boot-web-starter，新配置见ShrioConfiguration
 */
//@Configuration
public class ShiroConfig {

////	@Autowired
////	private Config config;
//
//	//地址为：cas地址
//	@Value("${shiro.cas}")
//	String casServerUrlPrefix;
//
//	//地址为：验证返回后的项目地址：http://localhost:8080
//	@Value("${shiro.server}")
//	String shiroServerUrlPrefix;
//
//	//相当于一个标志，可以随意，shiroConfig中也会用到
//	@Value("${pac4j.clientName}")
//	String clientName;
//
//	@Bean
//	public Config config() {
//
//		// CAS
//		final CasConfiguration configuration = new CasConfiguration(casServerUrlPrefix + "/login", casServerUrlPrefix);
//		//final CasConfiguration configuration = new CasConfiguration("/demo/login-cas", casServerUrlPrefix);
//		configuration.setAcceptAnyProxy(true);
//		CasClient casClient = new CasClient(configuration);
//		casClient.setCallbackUrl(shiroServerUrlPrefix + "/callback?client_name=" + clientName);
//		casClient.setName(clientName);
//
//		final Clients clients = new Clients(shiroServerUrlPrefix + "/callback?client_name=" + clientName, casClient);
//		final Config config = new Config(clients);
//		return config;
//	}
//
//
//	@Bean
//	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//		String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + "/callback?client_name=" + clientName;
//		//shiroFilterFactoryBean.setLoginUrl();
//
//		// 必须设置 SecurityManager
//		shiroFilterFactoryBean.setSecurityManager(securityManager);
//		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//		shiroFilterFactoryBean.setLoginUrl("/caslogin");
//		// 登录成功后要跳转的链接
//		shiroFilterFactoryBean.setSuccessUrl("/default");
//		// 未授权界面;
//		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//		//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//
//		// 权限控制map.
//		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//		// 配置不会被拦截的链接 顺序判断
//		filterChainDefinitionMap.put("/static/**", "anon");
//		filterChainDefinitionMap.put("/js/**", "anon");
//		filterChainDefinitionMap.put("/css/**", "anon");
//		filterChainDefinitionMap.put("/element/**", "anon");
//		///VerifyCode
//		filterChainDefinitionMap.put("/VerifyCode", "anon");
//		filterChainDefinitionMap.put("/caslogin", "anon");
//		//配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
//		//filterChainDefinitionMap.put("/logout", "logoutFilter");
//		//cas之用
//		filterChainDefinitionMap.put("/callback", "callbackFilter");
//
//		//filterChainDefinitionMap.put("/**", "casSecurityFilter");
//		//filterChainDefinitionMap.put("/**", "authc");
//		filterChainDefinitionMap.put("/**", "anon");
//		//filterChainDefinitionMap.put("/user", "perms[add1]");
//		//filterChainDefinitionMap.put("/**", "anon");
//
//
//		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//		shiroFilterFactoryBean.getFilters().putAll(filters());
//		//shiroFilterFactoryBean.setFilters(filters());
//
//		return shiroFilterFactoryBean;
//	}
//
//	/**
//	 * 对shiro的过滤策略进行明确
//	 * @return
//	 */
//	@Bean
//	protected Map<String, Filter> filters() {
//		//过滤器设置
//		Map<String, Filter> filters = new HashMap<>();
//		//filters.put("casSecurityFilter", casSecurityFilter());
//		CallbackFilter callbackFilter = new CallbackFilter();
//		callbackFilter.setConfig(config());
//		filters.put("callbackFilter", callbackFilter);
//		LogoutFilter logoutFilter = new LogoutFilter();
//		logoutFilter.setConfig(config());
//		filters.put("logoutFilter", logoutFilter);
//		return filters;
//	}
//
//	/**
//	 * cas核心过滤器，把支持的client写上，filter过滤时才会处理，clients必须在casConfig.clients已经注册
//	 *
//	 * @return
//	 */
//	@Bean
//	public Filter casSecurityFilter() {
//		SecurityFilter filter = new SecurityFilter();
//		filter.setClients(clientName);
//		filter.setConfig(config());
//		return filter;
//	}
//
//	@Bean
//	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//		DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
//		definition.addPathDefinition("/callback", "callbackFilter");
//		definition.addPathDefinition("/caslogin", "anon");
//		definition.addPathDefinition("/**", "casSecurityFilter");
//
//		return definition;
//	}
//
//	@Bean
//	public SecurityManager securityManager() {
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		// 设置realm.
//		//securityManager.setRealm(myShiroRealm());
//		//引入cas之用
//		//DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		Pac4jRealm casRealm = pac4jRealm();
//		securityManager.setRealm(casRealm);
//		securityManager.setSubjectFactory(subjectFactory());
//		// 自定义缓存实现 使用redis
//		//RedisCacheManager cache = cacheManager();
//		securityManager.setCacheManager(cacheManager());
//		// 自定义session管理 使用redis
//		securityManager.setSessionManager(SessionManager());
//
//		return securityManager;
//	}
//
//	@Bean
//	public MyShiroRealm myShiroRealm() {
//		MyShiroRealm myShiroRealm = new MyShiroRealm();
//		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//		return myShiroRealm;
//	}
//
//	@Bean
//	public HashedCredentialsMatcher hashedCredentialsMatcher(){
//		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//
//		hashedCredentialsMatcher.setHashAlgorithmName("SHA-1");//散列算法:这里使用SHA-1算法;
//		hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 SHA-1(SHA-1(""));
//
//		return hashedCredentialsMatcher;
//	}
//
//	/**
//	 * 配置shiro redisManager
//	 *
//	 * @return
//	 */
//	@Bean
//	public RedisManager redisManager() {
//		RedisManager redisManager = new RedisManager();
//		return redisManager;
//	}
//
//	/**
//	 * cacheManager 缓存 redis实现
//	 *
//	 * @return
//	 */
//	public RedisCacheManager cacheManager() {
//		RedisCacheManager redisCacheManager = new RedisCacheManager();
//		redisCacheManager.setRedisManager(redisManager());
//		return redisCacheManager;
//	}
//
//
//	/**
//	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
//	 */
//	public RedisSessionDAO redisSessionDAO() {
//		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
//		redisSessionDAO.setRedisManager(redisManager());
//		return redisSessionDAO;
//	}
//	/**
//	 * shiro session的管理
//	 */
//	public DefaultWebSessionManager SessionManager() {
//		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//		sessionManager.setSessionDAO(redisSessionDAO());
//		return sessionManager;
//	}
//
//	//cas begin
////	@Bean(name = "securityManager")
////	public SecurityManager securityManager() {
////
////		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
////		Pac4jRealm casRealm = pac4jRealm();
////		securityManager.setRealm(casRealm);
////		securityManager.setSubjectFactory(subjectFactory());
////		//securityManager.setCacheManager(ehCacheManager());
////		return securityManager;
////	}
//	@Bean(name = "pac4jRealm")
//	public Pac4jRealm pac4jRealm() {
//		//Pac4jRealm realm = new MyShiroRealm();
//		//Pac4jRealm myShiroRealm = new MyShiroRealm();
//		//return myShiroRealm;
//		return null;
//	}
//
//	@Bean(name = "subjectFactory")
//	public Pac4jSubjectFactory subjectFactory() {
//		Pac4jSubjectFactory subjectFactory = new Pac4jSubjectFactory();
//		return subjectFactory;
//	}
//	//cas end
//
//
//	//开启注解模式
//	@Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
//            = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
}
