package com.example.backend;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.backend.domain.repository.TodoRepository;
import com.example.backend.infra.repository.TodoRepositoryStub;
import com.example.fw.common.db.config.DynamicRoutingDataSourceConfig;
import com.example.fw.common.logging.config.LoggingConfigPackage;
import com.example.fw.common.metrics.config.MetricsConfig;

/**
 * 
 * インフラストラクチャ層の設定クラス
 *
 */
@Configuration
// Loggingの拡張設定を追加
@ComponentScan(basePackageClasses = { LoggingConfigPackage.class })
// 動的ルーティングによるデータソース設定、Micrometerのカスタムメトリックス設定を追加
@Import({ DynamicRoutingDataSourceConfig.class, MetricsConfig.class })
public class InfraConfig {

    // MyBatisでRDBアクセスしない場合のスタブ
    // @Bean
    TodoRepository todoRepository() {
        return new TodoRepositoryStub();
    }

}
