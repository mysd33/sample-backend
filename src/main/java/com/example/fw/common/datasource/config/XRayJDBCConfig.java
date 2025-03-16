package com.example.fw.common.datasource.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
//TODO: データソースの動的ルーティングへX-Rayトレーシング設定が対応するまでの暫定対処
@ConditionalOnProperty(prefix = "spring.datasource.dynamic-routing", name = "enabled", havingValue = "false", matchIfMissing = true)
@Configuration
public class XRayJDBCConfig {
    /**
     * DataSourceプロパティの取得
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * DataSourceでのAWS X-RayのJDBCトレーシング設定
     */
    @Bean
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
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
