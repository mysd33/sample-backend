package com.example.backend;

import org.springframework.context.annotation.Bean;
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

	//TODO: MyBatisでRDBアクセスするようにいずれ変更	
	@Bean
	public TodoRepository todoRepository() {
		return new TodoRepositoryStub();
	}

}
