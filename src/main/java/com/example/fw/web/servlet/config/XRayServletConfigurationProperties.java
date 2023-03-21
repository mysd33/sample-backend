package com.example.fw.web.servlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("aws.xray")
public class XRayServletConfigurationProperties {
    private String tracingFilterName; 
}
