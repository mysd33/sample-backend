package com.example.fw.common.db.utils;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessResourceFailureException;

import com.example.fw.common.exception.TransactionTimeoutBusinessException;

/**
 * データベースアクセスに関するユーティリティクラス。
 * 
 */
public final class DatabaseAccessUtils {
    // PostgreSQLのクエリーキャンセル（クエリータイムアウト時）のエラーコード57014
    private static final String QUERY_CANCELD_ERROR_CODE = "57014";

    private DatabaseAccessUtils() {
    }

    /**
     * 
     * データベースアクセス時に発生した例外がクエリタイムアウトによるものかを判定しビジネス例外に変換し返却する。<br>
     * それ以外の例外は元の例外のまま返却する。
     * 
     * @param e         DataAccessResourceFailureException
     * @param messageId クエリタイムアウトによるビジネス例外の場合に使用するメッセージID
     * @param args      クエリタイムアウトによるビジネス例外の場合に使用するメッセージ引数
     * 
     */
    public static RuntimeException convertToBusinessExceptionIfQueryTimeout(DataAccessResourceFailureException e,
            String messageId, String... args) {
        Throwable cause = e.getCause();
        // PostgreSQLのクエリータイムアウト時
        if (cause instanceof PSQLException psqlException
                && QUERY_CANCELD_ERROR_CODE.equals(psqlException.getSQLState())) {
            // BusinessExceptionでラップしてリスロー。
            return new TransactionTimeoutBusinessException(e, messageId, args);
        }
        return e; // それ以外は、そのまま元の例外をスロー
    }
}
