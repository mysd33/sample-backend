package com.example.fw.common.db.utils;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessResourceFailureException;

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
     * データベースアクセス時に発生した例外がクエリタイムアウトによるものかを判定する。<br>
     * 
     * @param e DataAccessResourceFailureException
     * @return trueの場合はクエリタイムアウトによる例外
     * 
     */
    public static boolean isQueryTimeout(DataAccessResourceFailureException e) {
        Throwable cause = e.getCause();
        // PostgreSQLのクエリータイムアウトかどうかを判定
        return cause instanceof PSQLException psqlException
                && QUERY_CANCELD_ERROR_CODE.equals(psqlException.getSQLState());
    }
}
