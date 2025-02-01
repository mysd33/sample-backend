package com.example.fw.web.servlet.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.jakarta.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.plugins.EKSPlugin;
import com.example.fw.web.aspect.XRayAspect;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * X-Ray設定クラス
 *
 */
@RequiredArgsConstructor
@Profile("xray")
@Configuration
@EnableConfigurationProperties({XRayServletConfigurationProperties.class})
public class XRayServletConfig  {
    private final XRayServletConfigurationProperties xRayServletConfigurationProperties;
    
    static {
        // サービスプラグインの設定
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new EKSPlugin())
                .withPlugin(new ECSPlugin()).withPlugin(new EC2Plugin());
        // TODO: サンプリングルール
        // URL ruleFile = WebConfig.class.getResource("/sampling-rules.json");
        // builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

        AWSXRay.setGlobalRecorder(builder.build());
    }

    /**
     * AWS X-RayのAOP設定
     */
    @Bean
    public XRayAspect xRayAspect() {
        return new XRayAspect();
    }
    
    
    /**
     * AWS X-Rayのトレーシングフィルタ設定
     * 
     */
    @Bean
    public Filter tracingFilter() {
        return new AWSXRayServletFilter(xRayServletConfigurationProperties.getTracingFilterName());
    }

}
