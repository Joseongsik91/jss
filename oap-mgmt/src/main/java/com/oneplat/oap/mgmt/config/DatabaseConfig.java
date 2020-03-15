package com.oneplat.oap.mgmt.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TODO DB설정 설정 처리 
 *
 * @author mike Ryu, BD Apis
 * @date 2015. 3. 09
 * @version 1.0
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
public class DatabaseConfig {
    @Autowired private Environment env;
     
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();       
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setValidationQuery(env.getProperty("spring.datasource.validationQuery"));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnBorrow")));
        dataSource.setTestOnReturn(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnReturn")));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("spring.datasource.testWhileIdle")));
        dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis")));
        dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("spring.datasource.minEvictableIdleTimeMillis")));
        dataSource.setMaxIdle(Integer.parseInt(env.getProperty("spring.datasource.maxIdle")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("spring.datasource.minIdle")));
        
        return dataSource;
    }
    

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
}