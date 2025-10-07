package com.example.fw.common.metrics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.fw.common.constants.FrameworkConstants;

import lombok.Data;

/**
 * メトリクス取得機能の設定プロパティクラス。
 */
@Data
@ConfigurationProperties(prefix = MetricsConfigurationProperties.PROPERTY_PREFIX)
public class MetricsConfigurationProperties {
    // メトリクス取得機能のプロパティのプレフィックス
    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "management.metrics";

    private MyBatisConfig mybatis = new MyBatisConfig();

    @Data
    public static class MyBatisConfig {
        /**
         * MyBatisのメトリクス観測を有効にするかどうかのフラグ
         */
        private boolean enable = true;

        private String meterNamePrefix = "mybatis.query";

    }

}
