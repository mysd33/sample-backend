package com.example.fw.common.rdb.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.example.fw.common.rdb.CustomRoutingDataSource;
import com.example.fw.common.rdb.CustomRoutingDataSource.DataSourceType;

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
    @ConfigurationProperties("spring.datasource.read")
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
    @ConfigurationProperties("spring.datasource.read.hikari")
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
       @ConfigurationProperties("spring.datasource.read")
       DataSource readDataSource() {
           return DataSourceBuilder.create().build();
       }
    // @formatter:on

    /**
     * クラスタエンドポイント接続用のDataSourceProperties
     */
    @Bean
    @ConfigurationProperties("spring.datasource.write")
    DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * クラスタエンドポイント接続用のDataSource
     */
    @Bean(defaultCandidate = false)
    // HikariCPを前提に、spring.datasource.hikariプロパティと同じ設定ができるようにする
    @ConfigurationProperties("spring.datasource.write.hikari")
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
