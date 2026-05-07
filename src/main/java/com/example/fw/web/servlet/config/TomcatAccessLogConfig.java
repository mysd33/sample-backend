package com.example.fw.web.servlet.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import com.example.fw.web.servlet.logback.LogMDCFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// Tomcatアクセスログの設定クラス
@ConditionalOnProperty(prefix = TomcatAccessLogConfigurationProperties.PROPERTY_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({TomcatAccessLogConfigurationProperties.class})
public class TomcatAccessLogConfig {

    private final TomcatAccessLogConfigurationProperties tomcatAccessLogConfigurationProperties;

    /// Tomcatのアクセスログの設定
    @Bean
    WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            var valve = new LogbackValve();
            valve.setFilename(tomcatAccessLogConfigurationProperties.getConfig());
            factory.addEngineValves(valve);
        };
    }

    /// Tomcatのアクセスログと通常のログをX-Amzn-Trace-Idで紐づけるためのフィルタ<br/>
    ///
    /// Spring Boot3.5以降ではアノテーションでFilterを直接登録可能となった
    @Bean
    @FilterRegistration
    LogMDCFilter logMDCFilter() {
        return new LogMDCFilter();
    }
}
