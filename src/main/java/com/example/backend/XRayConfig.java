package com.example.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.fw.common.jdbc.config.XRayJDBCConfig;
import com.example.fw.web.servlet.config.XRayServletConfig;

/**
 * X-Rayの設定クラス
 *
 */
// X-Ray機能の追加、JDBCのX-Rayトレース機能の追加
@Import({ XRayServletConfig.class, XRayJDBCConfig.class })
@Configuration
public class XRayConfig {
}
