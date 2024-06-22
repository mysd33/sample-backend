package com.example.fw.common.systemdate;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * システム日付取得機能のプロパティクラス
 *
 */
@Data
@ConfigurationProperties(prefix = "systemdate")
public class SystemDateConfigurationProperties {
    // テスト実施時、コマンドラインや環境変数等で、外部からシステム日付を固定化したい場合に利用
    // 指定する値の例：2020-01-01T12:00:00.123
    private String now;
}
