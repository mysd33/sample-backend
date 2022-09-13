package com.example.fw.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.fw.common.exception.SystemException;
import com.example.fw.common.message.ResultMessage;
import com.example.fw.common.message.ResultMessageType;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * 集約例外ハンドリングのためのControllerAdviceクラス
 *
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
	/**
	 * 例外発生時のデフォルトのメッセージID
	 */
	@Setter
	private String defaultExceptionMessageId;
	/**
	 * エラーページ表示用にModelに格納されるHTTPステータスの属性名
	 */
	@Setter
	private String statusModelAttributeName = "status";
	/**
	 * エラーページ名
	 */
	@Setter
	private String errorPageName = "error";

	@ExceptionHandler(SystemException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String systemExceptionHandler(final SystemException e, final Model model) {
		// 例外クラスのメッセージをModelに登録
		model.addAttribute(ResultMessage.builder().type(ResultMessageType.ERROR).code(e.getCode()).build());
		// HTTPのエラーコード（500）をModelに登録
		model.addAttribute(statusModelAttributeName, HttpStatus.INTERNAL_SERVER_ERROR);

		return errorPageName;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exceptionHandler(final Exception e, final Model model) {
		// 例外クラスのメッセージをModelに登録
		model.addAttribute(
				ResultMessage.builder().type(ResultMessageType.ERROR).code(defaultExceptionMessageId).build());
		// HTTPのエラーコード（500）をModelに登録
		model.addAttribute(statusModelAttributeName, HttpStatus.INTERNAL_SERVER_ERROR);

		return errorPageName;
	}
}
