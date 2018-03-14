package com.supdo.sb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class Application /*extends WebMvcConfigurationSupport*/{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 启用这个限制后静态资源不能使用，待确认原因。
     * kukei, 2017.12.27
     */
    /*	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}*/

}
