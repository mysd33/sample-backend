package com.example.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.backend.domain.repository.TodoRepository;
import com.example.backend.infra.repository.TodoRepositoryStub;
import com.example.fw.common.datasource.config.DynamicRoutingDataSourceConfig;

/**
 * 
 * インフラストラクチャ層の設定クラス
 *
 */
@Configuration
@Import(DynamicRoutingDataSourceConfig.class)
public class InfraConfig {

    // MyBatisでRDBアクセスしない場合のスタブ
    // @Bean
    TodoRepository todoRepository() {
        return new TodoRepositoryStub();
    }

}
