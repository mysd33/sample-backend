package com.example.fw.common.metrics.config;

import io.awspring.cloud.autoconfigure.metrics.CloudWatchAsyncClientCustomizer;
import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;

/**
 * Spring Cloud for AWSのAWS SDKクライアント設定
 */
@Configuration
@ConditionalOnProperty(prefix = "management.cloudwatch.metrics.export", name = "namespace")
@EnableConfigurationProperties(MetricsConfigurationProperties.class)
public class CloudWatchAsyncClientConfig {
    // https://docs.awspring.io/spring-cloud-aws/docs/4.0.0/reference/html/index.html#customizing-aws-clients
    // https://docs.awspring.io/spring-cloud-aws/docs/4.0.0/reference/html/index.html#client-customization-6

    /**
     * CloudWatchAsyncClientの設定のカスタマイズ
     */
    @Bean
    CloudWatchAsyncClientCustomizer customizer(
        MetricsConfigurationProperties metricsConfigurationProperties) {
        return builder -> builder
            .overrideConfiguration(o -> o.retryStrategy(RetryMode.STANDARD))
            .httpClient(NettyNioAsyncHttpClient.builder()
                .maxConcurrency(metricsConfigurationProperties.getMaxConnections())
                .connectionTimeout(
                    Duration.ofMillis(metricsConfigurationProperties.getConnectionTimeout()))
                .build());
    }
}
