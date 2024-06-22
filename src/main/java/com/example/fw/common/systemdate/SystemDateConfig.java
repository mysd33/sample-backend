package com.example.fw.common.systemdate;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * システム日付取得機能の設定クラス
 *
 */
@Configuration
@EnableConfigurationProperties({SystemDateConfigurationProperties.class})
public class SystemDateConfig {    
    
    @Bean
    public SystemDate systemDate(SystemDateConfigurationProperties systemDateConfigurationProperties) {
        return new DefaultSystemDate(systemDateConfigurationProperties.getNow());
    }

}
