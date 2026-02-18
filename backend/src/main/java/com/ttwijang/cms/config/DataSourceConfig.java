package com.ttwijang.cms.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	@Bean
    @ConfigurationProperties("spring.datasource.resource")
    @Primary
    public DataSource dataSource(){
        HikariDataSource ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        ds.setConnectionInitSql("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");
        return ds;
    }

    @Bean(name = "oauth")
    @ConfigurationProperties("spring.datasource.oauth")
    public DataSource dataSourceOAuth(){
        HikariDataSource ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        ds.setConnectionInitSql("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");
        return ds;
    }
}
