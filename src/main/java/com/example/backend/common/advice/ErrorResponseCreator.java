package com.example.backend.common.advice;

import java.util.ArrayList;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import com.example.backend.common.resource.ErrorResponse;
import com.example.fw.common.exception.ErrorCodeProvider;

import lombok.AllArgsConstructor;

/**
 * エラーレスポンスの作成クラス
 *
 */
@AllArgsConstructor
public class ErrorResponseCreator {
	private final MessageSource messageSource;
	private final String inputErrorMessageId;
	private final String unknowErrorMessageId;
	
	/**
	 * 入力エラーの場合のエラーレスポンスを作成する
	 * @param bindingResult BindingResult
	 * @param request WebRequest
	 * @return エラーレスポンス
	 */
	public ErrorResponse createInputErrorResponse(final BindingResult bindingResult, final WebRequest request) {
		//入力エラーの情報を詳細情報に格納し
		ArrayList<String> errorDetails = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String localizedMessage = messageSource.getMessage(fieldError, request.getLocale());
        	errorDetails.add(localizedMessage);
        }
        for (ObjectError objectError : bindingResult.getGlobalErrors()) {
            String localizedMessage = messageSource.getMessage(objectError, request.getLocale());
        	errorDetails.add(localizedMessage);
        } 		
		String message = messageSource.getMessage(inputErrorMessageId, null, request.getLocale()); 
        ErrorResponse body = ErrorResponse.builder().code(inputErrorMessageId).message(message).details(errorDetails).build();
		return body;
	}
	
	/**
	 * 業務エラー、システムエラーといった一般的なエラーのエラーレスポンスを作成する
	 * @param e ErrorCodeProviderインタフェースをもつ例外
	 * @param request WebRequest
	 * @return エラーレスポンス
	 */
	public ErrorResponse createGeneralErrorResponse(final ErrorCodeProvider e, final WebRequest request) {
		//例外が持つエラーコードとエラーコードにもとづくメッセージを返却
		String message = messageSource.getMessage(e.getCode(), e.getArgs(), request.getLocale()); 
    	return ErrorResponse.builder().code(e.getCode()).message(message).build();		
	}
	
	/**
	 * 予期せぬ例外によるエラーの場合のエラーレスポンスを作成する
	 * @param e 例外
	 * @param request WebRequest
	 * @return エラーレスポンス
	 */
	public ErrorResponse createUnknownErrorResponse(final Exception e, final WebRequest request) {
		//呼び出し元に例外の情報を必要以上に返却しないようデフォルトのメッセージを返却
    	String message = messageSource.getMessage(unknowErrorMessageId, null,  request.getLocale()); 
    	return ErrorResponse.builder().code(unknowErrorMessageId).message(message).build(); 		
	}

}
