package com.example.fw.common.logging.config;

import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.logging.MessageResourceExtensions;

/**
 * ロギング機能の設定クラス。
 */
@Configuration
public class LoggingExtensionConfig {

    /**
     * ログのメッセージ定義ファイルを拡張するためのクラス
     */
    @Bean
    MessageResourceExtensions messageResourceExtensions(MessageSourceProperties messageSourceProperties) {
        return new MessageResourceExtensions(messageSourceProperties);
    }

}
