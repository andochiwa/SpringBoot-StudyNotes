package com.github.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-1:05
 */
@Configuration
public class MyDataSourceConfig {

    @ConfigurationProperties("spring.datasource") // 把数据源信息和yml配置文件绑定
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }

}
