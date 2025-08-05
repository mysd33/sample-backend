package com.example.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.backend.domain.repository.TodoRepository;
import com.example.backend.infra.repository.TodoRepositoryStub;
import com.example.fw.common.db.config.DynamicRoutingDataSourceConfig;
import com.example.fw.common.micrometer.config.MicrometerConfig;

/**
 * 
 * インフラストラクチャ層の設定クラス
 *
 */
@Configuration
//動的ルーティングによるデータソース設定を追加
//Micrometerのカスタムメトリックス設定を追加
@Import({ DynamicRoutingDataSourceConfig.class, MicrometerConfig.class})
public class InfraConfig {

    // MyBatisでRDBアクセスしない場合のスタブ
    // @Bean
    TodoRepository todoRepository() {
        return new TodoRepositoryStub();
    }

}
