package com.example.fw.common.jdbc.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.xray.sql.TracingDataSource;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * X-Ray用のJDBCのトレーシング用設定クラス
 *
 */
@Profile("xray")
@Configuration
public class XRayJDBCConfig {
    /**
     * DataSourceプロパティの取得
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * DataSourceでのAWS X-RayのJDBCトレーシング設定
     */
    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        // @formatter:off
        return TracingDataSource.decorate(
                DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())               
                .build());
        // @formatter:on        
    }
}
