package com.example.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.fw.common.rdb.config.XRayJDBCConfig;
import com.example.fw.web.servlet.config.XRayServletConfig;

/// X-Rayの設定クラス
///
/// @deprecated X-Ray SDKは 2027 年 2 月 25 日にサポート終了となるため削除予定
@Deprecated(forRemoval = true)
// X-Ray機能の追加、JDBCのX-Rayトレース機能の追加
@Import({ XRayServletConfig.class, XRayJDBCConfig.class })
@Configuration
public class XRayConfig {
}
