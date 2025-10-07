package com.example.fw.common.rdb.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
     * 単一データソースの場合
     */
    @Configuration
    @ConditionalOnProperty(prefix = DynamicRoutingDataSourceConfig.DYNAMIC_ROUTING_PREFIX, name = "enabled", havingValue = "false", matchIfMissing = true)
    static class SingleDataSource {
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
        DataSource dataSourceForXray(DataSourceProperties dataSourceProperties) {
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

    /**
     * 動的データソースルーティングの場合
     */
    @Configuration
    @ConditionalOnProperty(prefix = DynamicRoutingDataSourceConfig.DYNAMIC_ROUTING_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    static class DynamicDataSource {

        /**
         * DataSourceでのAWS X-RayのJDBCトレーシング設定
         */
        @Bean
        @Primary // トレーシング用のDataSourceを優先
        DataSource dataSourceForXray(DataSource dataSource) {
            // DynamicRoutingDataSourceConfigでBean定義されたDataSourceをラップする
            return TracingDataSource.decorate(dataSource);
        }
    }
}
