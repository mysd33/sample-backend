package com.example.fw.web.auth;

/**
 * ログに出力するためのユーザ名を提供するインターフェース
 */
public interface UserNameProvider {
    /**
     * ユーザ名を取得する。
     * 
     * @return ユーザ名
     */
    String getUserName();
}
