package com.example.fw.common.metrics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "management.metrics")
public class MetricsConfigurationProperties {
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
