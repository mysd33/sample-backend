package com.example.fw.web.servlet.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            LogbackValve valve = new LogbackValve();
            valve.setFilename(tomcatAccessLogConfigurationProperties.getConfig());
            factory.addEngineValves(valve);
        };
    }

    @Bean
    public FilterRegistrationBean<LogMDCFilter> logMDCFilter() {
        return new FilterRegistrationBean<>(new LogMDCFilter());
    }

}
