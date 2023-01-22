package com.example.fw.common.logging;

/**
 * 
 * 業務ログ用出力インタフェース
 *
 */
public interface ApplicationLogger {
    /**
     * トレースログを出力する
     * 
     * @param messageId
     * @param args
     */
    void trace(String messageId, Object... args);

    /**
     * デバッグレベルが有効か
     * 
     * @return
     */
    boolean isDebugEnabled();

    /**
     * デバッグログを出力する
     * 
     * @param format
     * @param args
     */
    void debug(String format, Object... args);

    /**
     * 情報ログを出力する
     * 
     * @param messageId
     * @param args
     */
    void info(String messageId, Object... args);

    /**
     * 警告ログを出力する
     * 
     * @param messageId
     * @param args
     */
    void warn(String messageId, Object... args);

    /**
     * 警告ログを出力する
     * 
     * @param messageId
     * @param t
     * @param args
     */
    void warn(String messageId, Throwable t, Object... args);

    /**
     * 警告ログを出力する
     * 
     * @param messageId
     * @param format
     * @param t
     * @param args
     */
    void warn(String messageId, String format, Throwable t, Object... args);

}
