package com.example.fw.common.rdb.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.example.fw.common.constants.FrameworkConstants;
import com.example.fw.common.rdb.CustomRoutingDataSource;
import com.example.fw.common.rdb.CustomRoutingDataSource.DataSourceType;

/**
 * 動的にデータソース切替する機能の設定クラス
 */
@Configuration
@ConditionalOnProperty(prefix = DynamicRoutingDataSourceConfig.DYNAMIC_ROUTING_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicRoutingDataSourceConfig {
    // DBアクセス機能のプロパティプレフィックス
    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "datasource";
    static final String DYNAMIC_ROUTING_PREFIX = DynamicRoutingDataSourceConfig.PROPERTY_PREFIX + ".dynamic-routing";
    private static final String READER_PROPERTY_PREFIX = DynamicRoutingDataSourceConfig.PROPERTY_PREFIX + ".read";
    private static final String WRITER_PROPERTY_PREFIX = DynamicRoutingDataSourceConfig.PROPERTY_PREFIX + ".write";

    /**
     * リーダーエンドポイント接続用のDataSourceProperties
     */
    @Bean
    @ConfigurationProperties(READER_PROPERTY_PREFIX)
    DataSourceProperties readDataSourceProperties() {
        return new DataSourceProperties();
    }

    // TransactionやMyBatis関連のAutoConfigurationがうまく機能するよう
    // DataSourceのBean定義が複数あるため、DIされるDataSourceが1つだけになるようにする
    // メインのDataSource定義以外は、defaultCandidate = falseにしている
    // https://docs.spring.io/spring-boot/how-to/data-access.html#howto.data-access.configure-two-datasources

    /**
     * リーダーエンドポイント接続用のDataSource
     */
    @Bean(defaultCandidate = false)
    // HikariCPを前提に、spring.datasource.hikariプロパティと同じ設定ができるようにする
    @ConfigurationProperties(DynamicRoutingDataSourceConfig.READER_PROPERTY_PREFIX + ".hikari")
    DataSource readDataSource() {
        return readDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // @formatter:off
    // DataSourceBuilderを使うことで、以下のようにDataSourcePropertiesのBean定義なくすことも可能であるが、
    // HikariCPを使う場合は、HikariDataSourceに「url」プロパティがなく「jdbcUrl」プロパティとなるため
    // application.yamlの設定が「spring.datasource.read.jdbc-url」になってしまうため、採用していない
    // 
    // https://docs.spring.io/spring-boot/how-to/data-access.html#howto.data-access.configure-custom-datasource
    /*
       @Bean(defaultCandidate = false)
       @ConfigurationProperties(DynamicRoutingDataSourceConfig.READER_PROPERTY_PREFIX)
       DataSource readDataSource() {
           return DataSourceBuilder.create().build();
       }
    // @formatter:on

    /**
     * クラスタエンドポイント接続用のDataSourceProperties
     */
    @Bean
    @ConfigurationProperties(WRITER_PROPERTY_PREFIX)
    DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * クラスタエンドポイント接続用のDataSource
     */
    @Bean(defaultCandidate = false)
    // HikariCPを前提に、spring.datasource.hikariプロパティと同じ設定ができるようにする
    @ConfigurationProperties(DynamicRoutingDataSourceConfig.WRITER_PROPERTY_PREFIX + ".hikari")
    DataSource writeDataSource() {
        return writeDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * 動的ルーティング用のDataSource
     */
    @Bean(defaultCandidate = false)
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
    DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(customRoutingDataSource());
    }

}
