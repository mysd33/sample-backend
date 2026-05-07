package com.example.fw.common.exception;

/// エラーコードの情報を提供するインタフェース
public interface ErrorCodeProvider {

    /// エラーコードを取得する
    ///
    /// @return エラーコード
    String getCode();

    /// エラーコードに対応するメッセージの置換文字列を取得する
    ///
    /// @return 置換文字列
    String[] getArgs();
}
