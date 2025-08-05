package com.example.fw.common.db.utils;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessResourceFailureException;

import com.example.fw.common.exception.TransactionTimeoutBusinessException;

public final class DatabaseAccessUtils {
    private DatabaseAccessUtils() {
    }

    public static RuntimeException convertToBusinessExceptionIfTimeout(DataAccessResourceFailureException e,
            String messageId, String... args) {
        Throwable cause = e.getCause();
        // PostgreSQLのトランザクションタイムアウトエラーステートは57014
        // DataAccessResourceFailureExceptionがスローされるので、BusinessExceptionでラップしてリスロー。
        if (cause instanceof PSQLException psqlException && "57014".equals(psqlException.getSQLState())) {
            return new TransactionTimeoutBusinessException(e, messageId, args);
        }
        return e; // それ以外の例外はそのままスロー
    }

}
