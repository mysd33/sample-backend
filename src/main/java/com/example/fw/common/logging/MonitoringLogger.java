package com.example.fw.common.logging;

/// 監視ログ出力用インタフェース
public interface MonitoringLogger {

    /// エラーログを出力する
    ///
    /// @param messageId メッセージID
    /// @param args      置換文字列
    void error(String messageId, Object... args);

    /// エラーログを出力する
    ///
    /// @param messageId メッセージID
    /// @param t         例外
    /// @param args      置換文字列
    void error(String messageId, Throwable t, Object... args);

    /// エラーログを出力する
    ///
    /// @param messageId メッセージID
    /// @param format    フォーマット
    /// @param t         例外
    /// @param args      置換文字列
    void error(String messageId, String format, Throwable t, Object... args);

}
