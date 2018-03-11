package com.supdo.sb.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {

    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.main")
    @Primary
    public DataSource primaryDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "defaultJdbcTemplate")
    public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean(name = "defaultNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate defaultNamedParameterJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "crmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.crm")
    public DataSource crmDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "crmJdbcTemplate")
    public JdbcTemplate crmJdbcTemplate(@Qualifier("crmDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
