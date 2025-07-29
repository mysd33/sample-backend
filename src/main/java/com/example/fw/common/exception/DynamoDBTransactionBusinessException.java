package com.example.fw.common.exception;

import com.example.fw.common.message.ResultMessage;

/**
 * DynamoDBトランザクションの条件付き更新に失敗した場合等、業務エラーとして扱うための業務例外クラス<br>
 * 
 * アプリケーション層（RestControler）で発生する例外として区別するため、専用の例外を定義している。
 */
public class DynamoDBTransactionBusinessException extends BusinessException {

    private static final long serialVersionUID = -8962884948855517042L;

    public DynamoDBTransactionBusinessException(final String code) {
        super(code);
    }

    public DynamoDBTransactionBusinessException(final String code, final String... args) {
        super(code, args);
    }

    public DynamoDBTransactionBusinessException(final Throwable cause, final String code, final String... args) {
        super(cause, code, args);
    }

    public DynamoDBTransactionBusinessException(final ResultMessage resultMessage) {
        super(resultMessage);
    }

    public DynamoDBTransactionBusinessException(final Throwable cause, final ResultMessage resultMessage) {
        super(cause, resultMessage);
    }

}
