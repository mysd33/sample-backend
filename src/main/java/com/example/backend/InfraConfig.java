package com.example.backend;

import com.example.backend.domain.repository.TodoRepository;
import com.example.backend.infra.repository.TodoRepositoryStub;
import com.example.fw.common.logging.config.LoggingConfigPackage;
import com.example.fw.common.metrics.config.MetricsConfigPackage;
import com.example.fw.common.rdb.config.RDBConfigPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/// インフラストラクチャ層の設定クラス
@Configuration
// Loggingの拡張設定、RDBアクセスの動的データソースルーティング設定、メトリックス転送機能の設定を追加
@ComponentScan(basePackageClasses = {LoggingConfigPackage.class,
    MetricsConfigPackage.class, RDBConfigPackage.class}) //
public class InfraConfig {

    // MyBatisでRDBアクセスしない場合のスタブ
    // @Bean
    TodoRepository todoRepository() {
        return new TodoRepositoryStub();
    }

}
