package com.supdo.sb.demo.config;

import com.supdo.sb.demo.plugin.shiro.MyShiroRealm;
import com.supdo.sb.demo.plugin.shiro.RedisCacheManager;
import com.supdo.sb.demo.plugin.shiro.RedisManager;
import com.supdo.sb.demo.plugin.shiro.RedisSessionDAO;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShrioConfiguration extends AbstractShiroWebFilterConfiguration {

    @Value("#{ @environment['cas.prefixUrl'] ?: null }")
    private String prefixUrl;

    @Value("#{ @environment['cas.loginUrl'] ?: null }")
    private String casLoginUrl;

    @Value("#{ @environment['cas.callbackUrl'] ?: null }")
    private String callbackUrl;

    @Value("#{ @environment['cas.serviceUrl'] ?: null }")
    private String serviceUrl;


    /**
     * cas的基本设置，包括或url等等，rest调用协议等
     *
     * @return
     */
    @Bean
    public CasConfiguration casConfiguration() {
        CasConfiguration casConfiguration = new CasConfiguration(casLoginUrl);
        casConfiguration.setProtocol(CasProtocol.CAS30);
        casConfiguration.setPrefixUrl(prefixUrl);
        //casConfiguration.setPrefixUrl("http://127.0.0.1/sso");
        return casConfiguration;
    }

    /**
     * pac4jRealm
     *
     * @return
     */
    @Bean(name = "pac4jRealm")
    public Realm pac4jRealm() {
        return new MyShiroRealm();
    }

    /**
     * 通过rest接口可以获取tgt，获取service ticket，甚至可以获取CasProfile
     * @return
     */
    @Bean
    protected CasRestFormClient casRestFormClient() {
        CasRestFormClient casRestFormClient = new CasRestFormClient();
        casRestFormClient.setConfiguration(casConfiguration());
        casRestFormClient.setName("rest");
        return casRestFormClient;
    }

    /**
     * casClient
     *
     * @return
     */
    @Bean
    public CasClient casClient() {
        CasClient casClient = new CasClient();
        casClient.setConfiguration(casConfiguration());
        casClient.setCallbackUrl(callbackUrl);
        casClient.setName("cas");
        return casClient;
    }

    /**
     * token校验相关
     *
     * @return
     */
    @Bean
    protected Clients clients() {
        //可以设置默认client
        Clients clients = new Clients();
        //支持的client全部设置进去
        clients.setClients(casClient(), casRestFormClient());
        return clients;
    }

    @Bean
    protected Config casConfig() {
        Config config = new Config();
        config.setClients(clients());
        return config;
    }


    /**
     * 由于cas代理了用户，所以必须通过cas进行创建对象
     *
     * @return
     */
    @Bean(name = "subjectFactory")
    protected SubjectFactory subjectFactory() {
        return new Pac4jSubjectFactory();
    }

//    /**
//     * 单点登出的listener
//     *
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    @Bean
//    public ServletListenerRegistrationBean<?> singleSignOutHttpSessionListener() {
//        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
//        bean.setListener(new SingleSignOutHttpSessionListener());
//        bean.setEnabled(true);
//        return bean;
//    }
//
//    /**
//     * 单点登出filter
//     *
//     * @return
//     */
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public FilterRegistrationBean singleSignOutFilter() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setName("singleSignOutFilter");
//        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
//        singleSignOutFilter.setCasServerUrlPrefix(prefixUrl);
//        singleSignOutFilter.setIgnoreInitConfiguration(true);
//        bean.setFilter(singleSignOutFilter);
//        bean.addUrlPatterns("/*");
//        bean.setEnabled(true);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new DelegatingFilterProxy("shiroFilter"));
//        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
//        filterRegistrationBean.setEnabled(true);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(pac4jRealm());
        defaultWebSecurityManager.setSubjectFactory(subjectFactory());
        // 自定义缓存实现 使用redis
        //defaultWebSecurityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        defaultWebSecurityManager.setSessionManager(SessionManager());
        return defaultWebSecurityManager;
    }

    @Bean(name = "shiroFilter")
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {

        ShiroFilterFactoryBean filterFactoryBean = super.shiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager());

        filterFactoryBean.setFilters(shiroFilters());

        return filterFactoryBean;
    }

    /**
     * 对shiro的过滤策略进行明确
     * @return
     */
    @Bean
    protected Map<String, Filter> shiroFilters() {
        //过滤器设置
        Map<String, Filter> filters = new HashMap<String, Filter>();
        SecurityFilter securityFilter = new SecurityFilter();
        securityFilter.setClients("cas,rest");
        securityFilter.setConfig(casConfig());
        filters.put("casSecurityFilter", securityFilter);

        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(casConfig());
        filters.put("callbackFilter", callbackFilter);

        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setConfig(casConfig());
        logoutFilter.setCentralLogout(true);
        //此处直接写serviceUrl，logout后不会跳转，必须写其他地址
        logoutFilter.setDefaultUrl(serviceUrl+"/");
        filters.put("logoutFilter", logoutFilter);

        return filters;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        chainDefinition.addPathDefinition("/callback", "callbackFilter");
        chainDefinition.addPathDefinition("/logout", "logoutFilter");
        //chainDefinition.addPathDefinition("/caslogin", "anon");
        chainDefinition.addPathDefinition("/**", "casSecurityFilter");

        return chainDefinition;
    }

    @Bean
    protected CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
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

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("SHA-1");//散列算法:这里使用SHA-1算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 SHA-1(SHA-1(""));

        return hashedCredentialsMatcher;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启 shiro aop注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

}
