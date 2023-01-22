package com.example.fw.common.exception;

/**
 * エラーコードの情報を提供するインタフェース
 *
 */
public interface ErrorCodeProvider {
    /**
     * エラーコード
     * @return
     */
    String getCode();

    /**
     * エラーコードに対応するメッセージの置換文字列
     * @return
     */
    String[] getArgs();
}
