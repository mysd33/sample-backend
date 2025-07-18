package com.example.backend;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.backend.domain.message.MessageIds;
import com.example.fw.common.systemdate.SystemDate;
import com.example.fw.common.systemdate.config.SystemDateConfig;
import com.example.fw.web.advice.DefaultErrorResponseCreator;
import com.example.fw.web.advice.ErrorResponseCreator;
import com.example.fw.web.aspect.LogAspect;
import com.example.fw.web.servlet.config.TomcatAccessLogConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * 
 * アプリケーション層の設定クラス
 *
 */
@Configuration
//システム日時機能、Tomcatアクセスログ機能の追加
@Import({ SystemDateConfig.class, TomcatAccessLogConfig.class })
public class AppConfig {

    /**
     * エラーレスポンス作成クラス
     */
    @Bean
    ErrorResponseCreator errorResponseCreator(MessageSource messageSource) {
        return new DefaultErrorResponseCreator(messageSource, MessageIds.W_EX_5001, MessageIds.W_EX_5005,
                MessageIds.E_EX_9001);
    }

    /**
     * ロギング機能
     */
    @Bean
    LogAspect logAspect(SystemDate systemDate) {
        return new LogAspect(systemDate, MessageIds.E_EX_9001);
    }

    /**
     * Springdoc-openapiでスネークケースの設定が反映されるようにするための回避策
     */
    @Bean
    ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    /**
     * Springdoc-openapiの定義
     */
    @Bean
    OpenAPI springDocOpenAPI() {
        return new OpenAPI().info(new Info().title("Todo APIドキュメント").description("Todo管理のためのAPIです。").version("v1.0"));
    }

}
