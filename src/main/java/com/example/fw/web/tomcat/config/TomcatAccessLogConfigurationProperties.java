package com.example.fw.web.tomcat.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import com.example.fw.common.constants.FrameworkConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = TomcatAccessLogConfigurationProperties.PROPERTY_PREFIX)
public class TomcatAccessLogConfigurationProperties {

    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "logback.access";
    // logback-accessによるTomcatのアクセスログ出力を有効にするかどうか
    private boolean enabled = true;
    // logback-accessの設定ファイルのパス
    private String config = LogbackValve.DEFAULT_CONFIG_FILE;
}
