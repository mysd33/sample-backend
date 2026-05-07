package com.example.fw.common.logging;

/// 業務ログ用出力インタフェース
public interface ApplicationLogger {

    /// トレースログを出力する
    ///
    /// @param messageId メッセージID
    /// @param args      置換文字列
    void trace(String messageId, Object... args);

    /// デバッグレベルが有効か
    ///
    /// @return デバッグレベルが有効な場合はtrue
    boolean isDebugEnabled();

    /// デバッグログを出力する
    ///
    /// @param format フォーマット文字列
    /// @param args   置換文字列
    void debug(String format, Object... args);

    /// 情報ログを出力する
    ///
    /// @param messageId メッセージID
    /// @param args      置換文字列
    void info(String messageId, Object... args);

    /// 警告ログを出力する
    ///
    /// @param messageId メッセージID
    /// @param args      置換文字列
    void warn(String messageId, Object... args);

    /// 警告ログを出力する
    ///
    /// @param messageId メッセージID
    /// @param t         例外
    /// @param args      置換文字列
    void warn(String messageId, Throwable t, Object... args);

    /// 警告ログを出力する
    ///
    /// @param messageId メッセージID
    /// @param format    フォーマット
    /// @param t         例外
    /// @param args      置換文字列
    void warn(String messageId, String format, Throwable t, Object... args);

}
