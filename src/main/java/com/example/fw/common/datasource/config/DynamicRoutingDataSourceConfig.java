package com.example.fw.common.datasource.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.example.fw.common.datasource.CustomRoutingDataSource;
import com.example.fw.common.datasource.CustomRoutingDataSource.DataSourceType;

/**
 * 動的にデータソース切替する機能の設定クラス
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.dynamic-routing", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicRoutingDataSourceConfig {

    /**
     * リーダーエンドポイント接続用のDataSourceProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    DataSourceProperties readDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * リーダーエンドポイント接続用のDataSource
     */
    @Bean(autowireCandidate = false)
    DataSource readDataSource() {
        return readDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * クラスタエンドポイント接続用のDataSourceProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.write")
    DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * クラスタエンドポイント接続用のDataSource
     */
    @Bean(autowireCandidate = false)
    DataSource writeDataSource() {
        return writeDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * 動的ルーティング用のDataSource
     */
    @Bean(autowireCandidate = false)
    DataSource customRoutingDataSource() {
        CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();
        customRoutingDataSource.setTargetDataSources(//
                Map.of(DataSourceType.READ, readDataSource(), //
                        DataSourceType.WRITE, writeDataSource()));
        customRoutingDataSource.setDefaultTargetDataSource(writeDataSource());
        return customRoutingDataSource;
    }
    
    /**
     * メインのDataSource<br>
     * 
     * TransactionalアノテーションのreadOnly属性によってコネクションの振り分けができるよう遅延フェッチする
     * 
     */
    @Bean
    @Primary    // @Primaryアノテーションを付与することで、MybatisのAutoConfigurationによりSQLSessionFactoryがBean定義されるようにする
    DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(customRoutingDataSource());
    }

}
