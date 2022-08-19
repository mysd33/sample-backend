package com.example.fw.common.exception;

/**
 * エラーコードの情報を提供するインタフェース
 *
 */
public interface ErrorCodeProvider {
	String getCode();

	Object[] getArgs();
}
