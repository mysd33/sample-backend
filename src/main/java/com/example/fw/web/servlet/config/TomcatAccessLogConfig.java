package com.example.fw.web.servlet.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.web.servlet.logback.LogMDCFilter;

import ch.qos.logback.access.tomcat.LogbackValve;
import lombok.RequiredArgsConstructor;

/**
 * Tomcatアクセスログの設定クラス
 */
@ConditionalOnProperty(prefix = "logback.access", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({ TomcatAccessLogConfigurationProperties.class })
public class TomcatAccessLogConfig {
    private final TomcatAccessLogConfigurationProperties tomcatAccessLogConfigurationProperties;

    /**
     * Tomcatのアクセスログの設定
     * 
     */
    @Bean
    WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            LogbackValve valve = new LogbackValve();
            valve.setFilename(tomcatAccessLogConfigurationProperties.getConfig());
            factory.addEngineValves(valve);
        };
    }

    /**
     * Tomcatのアクセスログと通常のログをX-Amzn-Trace-Idで紐づけるためのフィルタ<br/>
     * 
     * Spring Boot3.5以降ではアノテーションでFilterを直接登録可能となった
     */
    @Bean
    @FilterRegistration
    LogMDCFilter logMDCFilter() {
        return new LogMDCFilter();
    }

    /**
     * Spring Boot3.4以前ではFilterRegistrationBeanを使用してFilterを登録する必要があった
     */
    /*
     * @Bean FilterRegistrationBean<LogMDCFilter> logMDCFilter() { return new
     * FilterRegistrationBean<>(new LogMDCFilter()); }
     */

}
