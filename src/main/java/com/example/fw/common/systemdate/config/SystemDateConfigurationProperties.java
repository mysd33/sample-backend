package com.example.fw.common.systemdate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.fw.common.constants.FrameworkConstants;

import lombok.Data;

/**
 * 
 * システム日付取得機能のプロパティクラス
 *
 */
@Data
@ConfigurationProperties(prefix = SystemDateConfigurationProperties.PROPERTY_PREFIX)
public class SystemDateConfigurationProperties {
    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "systemdate";

    // テスト実施時、コマンドラインや環境変数等で、外部からシステム日付を固定化したい場合に利用
    // 指定する値の例：2020-01-01T12:00:00.123
    private String now;
}
