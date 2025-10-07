package com.example.fw.web.servlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.fw.common.constants.FrameworkConstants;

import ch.qos.logback.access.tomcat.LogbackValve;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = TomcatAccessLogConfigurationProperties.PROPERTY_PREFIX)
public class TomcatAccessLogConfigurationProperties {
    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "logback.access";
    // logback-accessによるTomcatのアクセスログ出力を有効にするかどうか
    private boolean enabled = true;
    // logback-accessの設定ファイルのパス
    private String config = LogbackValve.DEFAULT_CONFIG_FILE;
}
