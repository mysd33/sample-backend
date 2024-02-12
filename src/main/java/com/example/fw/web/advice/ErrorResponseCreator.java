package com.example.fw.web.advice;

import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;

public interface ErrorResponseCreator {

    /**
     * 入力エラーの場合のエラーレスポンスを作成する
     * 
     * @param bindingResult BindingResult
     * @param request       WebRequest
     * @return エラーレスポンス
     */
    Object createValidationErrorResponse(BindingResult bindingResult, WebRequest request);


    /**
     * 業務エラーのエラーレスポンスを作成する
     * 
     * @param e       BusinessException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    Object createBusinessErrorResponse(BusinessException e, WebRequest request);

    
    /**
     * 警告エラーのエラーレスポンスを作成する
     * @param e
     * @param statusCode
     * @param request
     * @return
     */
    Object createWarnErrorResponse(Exception e, HttpStatusCode statusCode, WebRequest request);
    
    
    /**
     * 業務エラー、システムエラーといった一般的なエラーのエラーレスポンスを作成する
     * 
     * @param e       SystemException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    Object createSystemErrorResponse(SystemException e, WebRequest request); 
    
    /**
     * 予期せぬ例外によるエラーの場合のエラーレスポンスを作成する
     * 
     * @param e       例外
     * @param request WebRequest
     * @return エラーレスポンス
     */
    Object createUnexpectedErrorResponse(Exception e, WebRequest request);

}