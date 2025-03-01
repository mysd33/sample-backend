package com.example.backend.app.api.common.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import com.example.backend.app.api.common.resource.ErrorResponse;
import com.example.backend.domain.message.MessageIds;
import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.ErrorCodeProvider;
import com.example.fw.common.exception.SystemException;
import com.example.fw.web.advice.ErrorResponseCreator;
import com.example.fw.web.advice.InvalidFormatField;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

/**
 * エラーレスポンスの作成クラス
 *
 */
@RequiredArgsConstructor
public class DefaultErrorResponseCreator implements ErrorResponseCreator {
    private final MessageSource messageSource;
    private final String inputErrorMessageId;
    private final String unknowErrorMessageId;

    /**
     * 入力エラー（リクエストメッセージのJSONが不正な構文でパースに失敗）の場合のエラーレスポンスを作成する
     * 
     * @param e       JsonParseException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createRequestParseErrorResponse(JsonParseException e, WebRequest request) {
        ArrayList<String> errorDetails = new ArrayList<>();
        String localizedMessage = messageSource.getMessage(MessageIds.W_EX_5002, null, request.getLocale());
        errorDetails.add(localizedMessage);
        String message = messageSource.getMessage(inputErrorMessageId, null, request.getLocale());
        return ErrorResponse.builder().code(inputErrorMessageId).message(message).details(errorDetails).build();
    }

    /**
     * 入力エラー（リクエストメッセージからResourceオブジェクトへの変換に失敗）の場合のエラーレスポンスを作成する
     * 
     * @param invalidFields JsonMappingExceptinonからエラーの原因となフィールドのリストを取得したもの
     * @param e             JsonMappingException
     * @param request       WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createRequestMappingErrorResponse(List<InvalidFormatField> invalidFields, JsonMappingException e,
            WebRequest request) {

        ArrayList<String> errorDetails = new ArrayList<>();
        invalidFields.forEach(field -> {
            if (StringUtils.hasLength(field.getDescription())) {
                String localizedMessage = messageSource.getMessage(MessageIds.W_EX_5003,
                        new Object[] { field.getDescription(), field.getFieldName() }, request.getLocale());
                errorDetails.add(localizedMessage);
            } else {
                String localizedMessage = messageSource.getMessage(MessageIds.W_EX_5004,
                        new Object[] { field.getFieldName() }, request.getLocale());
                errorDetails.add(localizedMessage);
            }
        });
        String message = messageSource.getMessage(inputErrorMessageId, null, request.getLocale());
        return ErrorResponse.builder().code(inputErrorMessageId).message(message).details(errorDetails).build();
    }

    /**
     * 入力エラー（Validationエラー）の場合のエラーレスポンスを作成する
     * 
     * @param bindingResult BindingResult
     * @param request       WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createValidationErrorResponse(final BindingResult bindingResult, final WebRequest request) {
        // 入力エラーの情報を詳細情報に格納し
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
        return ErrorResponse.builder().code(inputErrorMessageId).message(message).details(errorDetails).build();
    }

    /**
     * 業務エラーのエラーレスポンスを作成する
     * 
     * @param e       BusinessException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createBusinessErrorResponse(final BusinessException e, final WebRequest request) {
        return createGeneralErrorResponse(e, request);
    }

    /**
     * 警告エラーのエラーレスポンスを作成する
     * 
     * @param e
     * @param statusCode
     * @param request
     * @return
     */
    @Override
    public Object createWarnErrorResponse(Exception e, HttpStatusCode statusCode, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        return ErrorResponse.builder().code(String.valueOf(statusCode.value())).message(status.name()).build();
    }

    /**
     * 業務エラー、システムエラーといった一般的なエラーのエラーレスポンスを作成する
     * 
     * @param e       SystemException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createSystemErrorResponse(final SystemException e, final WebRequest request) {
        return createGeneralErrorResponse(e, request);
    }

    /**
     * 予期せぬ例外によるエラーの場合のエラーレスポンスを作成する
     * 
     * @param e       例外
     * @param request WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createUnexpectedErrorResponse(final Exception e, final WebRequest request) {
        // 呼び出し元に例外の情報を必要以上に返却しないようデフォルトのメッセージを返却
        String message = messageSource.getMessage(unknowErrorMessageId, null, request.getLocale());
        return ErrorResponse.builder().code(unknowErrorMessageId).message(message).build();
    }

    /**
     * 業務エラー、システムエラーといった一般的なエラーのエラーレスポンスを作成する
     * 
     * @param e       ErrorCodeProviderインタフェースをもつ例外
     * @param request WebRequest
     * @return エラーレスポンス
     */
    private ErrorResponse createGeneralErrorResponse(final ErrorCodeProvider e, final WebRequest request) {
        // 例外が持つエラーコードとエラーコードにもとづくメッセージを返却
        String message = messageSource.getMessage(e.getCode(), e.getArgs(), request.getLocale());
        return ErrorResponse.builder().code(e.getCode()).message(message).build();
    }
}
