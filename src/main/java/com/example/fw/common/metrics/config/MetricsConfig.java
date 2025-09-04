package com.example.fw.common.metrics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.metrics.MyBatisMetricsObserver;

import io.micrometer.observation.ObservationRegistry;

/**
 * Micrometerのカスタムメトリックス設定クラス
 */
@Configuration
public class MetricsConfig {

    /**
     *  MyBatisのメトリクス観測用のBean定義
     */
    @Bean
    MyBatisMetricsObserver myBatisMetricsObserver(ObservationRegistry observationRegistry) {
        return new MyBatisMetricsObserver(observationRegistry);
    }

}
