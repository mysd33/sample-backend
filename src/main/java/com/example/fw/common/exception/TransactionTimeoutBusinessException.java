package com.example.fw.common.exception;

import com.example.fw.common.message.ResultMessage;

/**
 * RDBトランザクション処理でタイムアウト発生した場合に業務エラー扱いとするための業務例外クラス<br>
 * 
 * アプリケーション層（RestControler）で発生する例外として区別するため、専用の例外を定義している。
 */
public class TransactionTimeoutBusinessException extends BusinessException {

    private static final long serialVersionUID = -1744348321677818805L;

    public TransactionTimeoutBusinessException(final String code) {
        super(code);
    }

    public TransactionTimeoutBusinessException(final String code, final String... args) {
        super(code, args);
    }

    public TransactionTimeoutBusinessException(final Throwable cause, final String code, final String... args) {
        super(cause, code, args);
    }

    public TransactionTimeoutBusinessException(final ResultMessage resultMessage) {
        super(resultMessage);
    }

    public TransactionTimeoutBusinessException(final Throwable cause, final ResultMessage resultMessage) {
        super(cause, resultMessage);
    }

}
