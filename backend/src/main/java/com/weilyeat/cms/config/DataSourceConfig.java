package com.weilyeat.cms.config;

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
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "oauth")
    @ConfigurationProperties("spring.datasource.oauth")
    public DataSource dataSourceOAuth(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
