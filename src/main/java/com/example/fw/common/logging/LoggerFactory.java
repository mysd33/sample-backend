package com.example.fw.common.logging;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 
 * 本システム専用のLoggerを作成するFactoryクラス
 *
 */
public final class LoggerFactory {
    private static ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    static {
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("messages", "messages-fw-common", "messages-fw-web", "messages-fw-batch");
    }

    private LoggerFactory() {
    }

    /**
     * 監査ログ出力オブジェクトを返却する
     * 
     * @param delegateLogger SLF4Jロガー
     */
    public static AuditLogger getAuditLogger(final Logger delegateLogger) {
        return new DefaultLogger(delegateLogger, messageSource);
    }

    /**
     * アプリケーションログ出力オブジェクトを返却する
     * 
     * @param delegateLogger SLF4Jロガー
     */
    public static ApplicationLogger getApplicationLogger(final Logger delegateLogger) {
        return new DefaultLogger(delegateLogger, messageSource);
    }

    /**
     * 監視ログ出力オブジェクトを返却する
     * 
     * @param delegateLogger SLF4Jロガー
     */
    public static MonitoringLogger getMonitoringLogger(final Logger delegateLogger) {
        return new DefaultLogger(delegateLogger, messageSource);
    }

    /**
     * 
     * @param baseNames メッセージ定義ファイルのベース名を追加する
     */
    static void addMessageSourceBaseName(final List<String> baseNames) {
        messageSource.addBasenames(baseNames.toArray(new String[baseNames.size()]));
    }
}
