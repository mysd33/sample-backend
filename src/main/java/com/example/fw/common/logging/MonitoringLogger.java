package com.example.fw.common.logging;

/**
 * 監視ログ出力用インタフェース
 */
public interface MonitoringLogger {

    /**
     * エラーログを出力する
     * 
     * @param messageId
     * @param args
     */
    void error(String messageId, Object... args);

    /**
     * エラーログを出力する
     * 
     * @param messageId
     * @param t
     * @param args
     */
    void error(String messageId, Throwable t, Object... args);

    /**
     * エラーログを出力する
     * 
     * @param messageId
     * @param format
     * @param t
     * @param args
     */
    void error(String messageId, String format, Throwable t, Object... args);

}
