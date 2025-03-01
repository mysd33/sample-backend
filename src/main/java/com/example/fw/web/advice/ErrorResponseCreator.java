package com.example.fw.web.advice;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * エラーレスポンスの作成インタフェース
 *
 */
public interface ErrorResponseCreator {
    /**
     * 入力エラー（リクエストメッセージのJSONが不正な構文でパースに失敗）の場合のエラーレスポンスを作成する
     * 
     * @param e       JsonParseException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    Object createRequestFormatErrorResponse(JsonParseException e, WebRequest request);

    /**
     * 入力エラー（リクエストメッセージからResourceオブジェクトへの変換に失敗）の場合のエラーレスポンスを作成する
     * 
     * @param invalidFields JsonMappingExceptinonからエラーの原因となフィールドのリストを取得したもの
     * @param e             JsonMappingException
     * @param request       WebRequest
     * @return エラーレスポンス
     */
    Object createRequestMappingErrorResponse(List<InvalidFormatField> invalidFields, JsonMappingException e,
            WebRequest request);

    /**
     * 入力エラー（Validationエラー）の場合のエラーレスポンスを作成する
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
     * 
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