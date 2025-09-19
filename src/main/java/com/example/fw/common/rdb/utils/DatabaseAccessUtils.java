package com.example.fw.common.rdb.utils;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * データベースアクセスに関するユーティリティクラス。
 * 
 */
public final class DatabaseAccessUtils {
    private DatabaseAccessUtils() {
    }

    /**
     * 
     * データベースアクセス時に発生した例外がクエリタイムアウトによるものかを判定する。<br>
     * 
     * @param e DataAccessResourceFailureException
     * @return trueの場合はクエリタイムアウトによる例外
     * 
     */
    public static boolean isQueryTimeout(DataAccessResourceFailureException e) {
        Throwable cause = e.getCause();
        // PostgreSQLのクエリータイムアウトかどうかを判定
        return (cause instanceof PSQLException psqlException)
                && PSQLState.QUERY_CANCELED.getState().equals(psqlException.getSQLState());
    }
}
