package com.example.fw.common.micrometer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.micrometer.MyBatisMetricsObserver;

import io.micrometer.observation.ObservationRegistry;

/**
 * Micrometerのカスタムメトリックス設定クラス
 */
@Configuration
public class MicrometerConfig {

    /**
     *  MyBatisのメトリクス観測用のBean定義
     */
    @Bean
    MyBatisMetricsObserver myBatisMetricsObserver(ObservationRegistry observationRegistry) {
        return new MyBatisMetricsObserver(observationRegistry);
    }

}
