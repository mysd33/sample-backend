package com.example.fw.web.servlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.fw.common.constants.FrameworkConstants;

import lombok.Data;

/**
 * X-Rayの設定プロパティクラス
 *
 */
@Data
@ConfigurationProperties(XRayServletConfigurationProperties.PROPERTY_PREFIX)
public class XRayServletConfigurationProperties {
    // X-Rayの設定のプロパティプレフィックス
    static final String PROPERTY_PREFIX = FrameworkConstants.PROPERTY_BASE_NAME + "xray";

    // トレーシングフィルタの名前
    private String tracingFilterName;
}
