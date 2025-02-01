package com.example.fw.web.servlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ch.qos.logback.access.tomcat.LogbackValve;

import lombok.Data;

@Data
@ConfigurationProperties("logback.access")
public class TomcatAccessLogConfigurationProperties {
    // logback-accessによるTomcatのアクセスログ出力を有効にするかどうか
    private boolean enabled = true;
    // logback-accessの設定ファイルのパス
    private String config = LogbackValve.DEFAULT_CONFIG_FILE;
}
