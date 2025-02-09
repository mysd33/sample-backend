package com.example.backend;

import org.springframework.context.annotation.Configuration;

import com.example.backend.domain.repository.TodoRepository;
import com.example.backend.infra.repository.TodoRepositoryStub;

/**
 * 
 * インフラストラクチャ層の設定クラス
 *
 */
@Configuration
public class InfraConfig {

	//MyBatisでRDBアクセスしない場合のスタブ	
	//@Bean
	TodoRepository todoRepository() {
		return new TodoRepositoryStub();
	}

}
