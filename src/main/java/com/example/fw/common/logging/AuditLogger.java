package com.example.fw.common.logging;

/// 監査ログ出力用インタフェース
public interface AuditLogger {

    /// 監査ログを出力する
    ///
    /// @param messageId メッセージID
    /// @param args      置換文字列
    void audit(String messageId, Object... args);
}
