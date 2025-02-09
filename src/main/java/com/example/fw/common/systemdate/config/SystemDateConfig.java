package com.example.fw.common.systemdate.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.systemdate.DefaultSystemDate;
import com.example.fw.common.systemdate.SystemDate;

/**
 * システム日付取得機能の設定クラス
 *
 */
@Configuration
@EnableConfigurationProperties({SystemDateConfigurationProperties.class})
public class SystemDateConfig {    
    
    @Bean
    SystemDate systemDate(SystemDateConfigurationProperties systemDateConfigurationProperties) {
        return new DefaultSystemDate(systemDateConfigurationProperties.getNow());
    }

}
