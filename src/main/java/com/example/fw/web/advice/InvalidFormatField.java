package com.example.fw.web.advice;

import lombok.Builder;
import lombok.Data;

/**
 * リクエストメッセージのJavaBean変換に失敗時のフィールド情報
 */
@Data
@Builder
public class InvalidFormatField {
    public enum ErrorType {
        // 規定されないフィールド
        UNRECOGNIZED_FIELD,
        // フィールドのフォーマットが不正
        INVALID_FORMAT,
    }

    // フィールド名
    private String fieldName;
    // フィールドの説明（日本語のラベル）
    private String description;
    // エラーの種類
    private ErrorType errorType;

}
