package com.example.fw.common.metrics.config;

import com.example.fw.common.constants.FrameworkConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// メトリクス取得機能の設定プロパティクラス。
@Data
@ConfigurationProperties(prefix = MetricsConfigurationProperties.PROPERTY_PREFIX)
public class MetricsConfigurationProperties {

    // メトリクス取得機能のプロパティのプレフィックス
    static final String PROPERTY_PREFIX =
        FrameworkConstants.PROPERTY_BASE_NAME + "management.metrics";

    // MyBatisに関する設定
    private MyBatisConfig mybatis = new MyBatisConfig();

    // HTTPコネクションプールの最大接続数（AWS SDKのデフォルト値50）
    // https://github.com/aws/aws-sdk-java-v2/blob/master/http-client-spi/src/main/java/software/amazon/awssdk/http/SdkHttpConfigurationOption.java#L151
    private int maxConnections = 50;
    // HTTPコネクション確立時のタイムアウト（ミリ秒。AWS SDKのデフォルト値2秒 = 2000ミリ秒）
    // https://github.com/aws/aws-sdk-java-v2/blob/master/http-client-spi/src/main/java/software/amazon/awssdk/http/SdkHttpConfigurationOption.java#L142
    private int connectionTimeout = 2000;

    @Data
    public static class MyBatisConfig {

        /// MyBatisのメトリクス観測を有効にするかどうかのフラグ
        private boolean enable = true;

        private String meterNamePrefix = "mybatis.query";

    }

}
