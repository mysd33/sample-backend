package com.example.fw.web.advice;


import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;
import com.example.fw.web.resource.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * 集約例外ハンドリングのためのRestControllerAdviceクラス
 *
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestControllerAdvice {
	private final MessageSource messageSource;
	
	@Setter
	private String defaultExceptionMessageId;
		
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bussinessExceptionHandler(final BusinessException e) {
    	String message = messageSource.getMessage(e.getCode(), e.getArgs(), Locale.getDefault()); 
        return ErrorResponse.builder().code(e.getCode()).message(message).build();
    }
    
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse systemExceptionHandler(final SystemException e) {
    	String message = messageSource.getMessage(e.getCode(), e.getArgs(), Locale.getDefault()); 
    	return ErrorResponse.builder().code(e.getCode()).message(message).build();
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(final Exception e) {
    	String message = messageSource.getMessage(defaultExceptionMessageId, null,  Locale.getDefault()); 
    	return ErrorResponse.builder().code(defaultExceptionMessageId).message(message).build();        
    }
}
