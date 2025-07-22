package com.example.fw.web.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.context.request.WebRequest;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.ErrorCodeProvider;
import com.example.fw.common.exception.SystemException;
import com.example.fw.web.message.WebFrameworkMessageIds;
import com.example.fw.web.resource.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Builder;

/**
 * エラーレスポンスの作成クラス
 *
 */
@Builder
public class DefaultErrorResponseCreator implements ErrorResponseCreator {
    private static final String PLACEHOLDER_ZERO = "{0}";
    private final MessageSource messageSource;
    // 入力エラーのメッセージID
    private final String inputErrorMessageId;
    // 予期せぬエラーのメッセージID
    private final String unknowErrorMessageId;
    // 入力エラーのうちリクエストボディのバリデーションエラーのメッセージID
    private final String requestBodyValidationErrorMessageId;

    /**
     * コンストラクタ（オプション引数あり）
     * 
     * @param messageSource                      メッセージソース
     * @param inputErrorMessageId                入力エラーのメッセージID
     * @param unknowErrorMessageId               予期せぬエラーのメッセージID
     * @param inputErrorMessageIdWithPlaceholder Resourceクラスの日本語のラベル名をプレースホルダ{0}として付与する入力エラーのメッセージID。オプションで指定可能。
     */
    public DefaultErrorResponseCreator(final MessageSource messageSource, final String inputErrorMessageId,
            final String unknowErrorMessageId, final String inputErrorMessageIdWithPlaceholder) {
        this.messageSource = messageSource;
        this.inputErrorMessageId = inputErrorMessageId;
        this.unknowErrorMessageId = unknowErrorMessageId;
        this.requestBodyValidationErrorMessageId = inputErrorMessageIdWithPlaceholder;
    }

    /**
     * コンストラクタ
     * <p>
     * inputErrorMessageIdWithPlaceholderオプション引数を使用しない場合は、inputErrorMessageIdが使用される
     * </p>
     * 
     * @param messageSource        メッセージソース
     * @param inputErrorMessageId  入力エラーのメッセージID
     * @param unknowErrorMessageId 予期せぬエラーのメッセージID
     * 
     */
    public DefaultErrorResponseCreator(final MessageSource messageSource, final String inputErrorMessageId,
            final String unknowErrorMessageId) {
        this.messageSource = messageSource;
        this.inputErrorMessageId = inputErrorMessageId;
        this.unknowErrorMessageId = unknowErrorMessageId;
        this.requestBodyValidationErrorMessageId = inputErrorMessageId;
    }

    /**
     * 入力エラー（パスパラメータやクエリパラメータのバリデーションエラー）の場合のエラーレスポンスを作成する
     */
    @Override
    public Object createParameterValidationErrorResponse(
            final List<ParameterValidationResult> parameterValidationResults, final WebRequest request) {
        // 入力エラーの情報を詳細情報に格納
        ArrayList<String> errorDetails = new ArrayList<>();
        for (ParameterValidationResult result : parameterValidationResults) {
            String parameterLabel = "";
            // パラメータ名を取得
            String parameterName = result.getMethodParameter().getParameterName();
            if (parameterName != null) {
                // パラメータ名に対するメッセージ（＝パラメータのラベル名）を取得
                parameterLabel = messageSource.getMessage(parameterName, null, request.getLocale());
                if (!StringUtils.hasLength(parameterLabel)) {
                    // パラメータ名がメッセージソースに登録されていない場合は、パラメータ名をそのまま使用
                    parameterLabel = parameterName;
                }
            }

            List<MessageSourceResolvable> errors = result.getResolvableErrors();
            for (MessageSourceResolvable error : errors) {
                // 各エラーのメッセージ取得
                String message = error.getDefaultMessage();

                // メッセージに{0}を含むか正規表現でチェックして置換
                if (StringUtils.hasLength(message) && message.contains(PLACEHOLDER_ZERO)) {
                    // {0}をパラメータ名に置換
                    message = message.replace(PLACEHOLDER_ZERO, parameterLabel);
                }
                errorDetails.add(message);
            }
        }
        String message = messageSource.getMessage(inputErrorMessageId, null, request.getLocale());
        return ErrorResponse.builder().code(inputErrorMessageId).message(message).details(errorDetails).build();

    }

    /**
     * 入力エラー（リクエストメッセージのJSONが不正な構文でパースに失敗）の場合のエラーレスポンスを作成する
     * 
     * @param e       JsonParseException
     * @param request WebRequest
     * @return エラーレスポンス
     */
    @Override
    public Object createRequestParseErrorResponse(final JsonParseException e, final WebRequest request) {
        ArrayList<String> errorDetails = new ArrayList<>();
        String localizedMessage = messageSource.getMessage(WebFrameworkMessageIds.W_ON_FW_2001, null,
                request.getLocale());
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
    public Object createRequestMappingErrorResponse(final List<InvalidFormatField> invalidFields,
            final JsonMappingException e, final WebRequest request) {

        ArrayList<String> errorDetails = new ArrayList<>();
        invalidFields.forEach(field -> {
            if (StringUtils.hasLength(field.getDescription())) {
                String localizedMessage = messageSource.getMessage(WebFrameworkMessageIds.W_ON_FW_2002,
                        new Object[] { field.getDescription(), field.getFieldName() }, request.getLocale());
                errorDetails.add(localizedMessage);
            } else {
                String localizedMessage = messageSource.getMessage(WebFrameworkMessageIds.W_ON_FW_2003,
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
        // 入力エラーの情報を詳細情報に格納
        ArrayList<String> errorDetails = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String localizedMessage = messageSource.getMessage(fieldError, request.getLocale());
            errorDetails.add(localizedMessage);
        }
        for (ObjectError objectError : bindingResult.getGlobalErrors()) {
            String localizedMessage = messageSource.getMessage(objectError, request.getLocale());
            errorDetails.add(localizedMessage);
        }
        // {0}を含むエラーメッセージIDからメッセージを取得
        String message = messageSource.getMessage(requestBodyValidationErrorMessageId, null, request.getLocale());
        // Bean全体に対する日本語名を取得
        String objectLabel = getObjectLabel(bindingResult, request);
        // メッセージに{0}を含むか正規表現でチェックして置換
        if (StringUtils.hasLength(message) && message.contains(PLACEHOLDER_ZERO)) {
            // {0}をパラメータ名に置換
            message = message.replace(PLACEHOLDER_ZERO, objectLabel);
        }
        // この例では、inputErrorMessageIdWithPlaceholderをコードとして返却
        // 他の入力エラーとコードを統一したい場合はinputErrorMessageIdにするとよい）
        return ErrorResponse.builder().code(requestBodyValidationErrorMessageId).message(message).details(errorDetails)
                .build();
    }

    /**
     * BindingResultからオブジェクト名に対する日本語ラベルを取得する
     * 
     * @param bindingResult BindingResult
     * @param request       WebRequest
     * @return オブジェクト名に対する日本語ラベル
     */
    private String getObjectLabel(final BindingResult bindingResult, final WebRequest request) {
        // オブジェクト名を取得
        String objectName = bindingResult.getObjectName();
        // メッセージ定義からオブジェクト名に対する日本語ラベルを取得
        String objectLabel = messageSource.getMessage(objectName, null, request.getLocale());
        if (StringUtils.hasLength(objectLabel)) {
            return objectLabel;
        }
        Object target = bindingResult.getTarget();
        if (target == null) {
            // ターゲットがnullの場合は、オブジェクト名をそのまま使用
            return objectName;
        }
        // オブジェクト名で登録されていない場合は、オブジェクトのクラス名で日本語ラベルを取得
        Class<?> targetClass = target.getClass();
        String targetClassName = targetClass.getSimpleName();
        objectLabel = messageSource.getMessage(targetClassName, null, request.getLocale());
        if (StringUtils.hasLength(objectLabel)) {
            return objectLabel;
        }
        // クラス名も登録されていない場合は、クラスのFQDNを使用して日本語ラベルを取得
        String targetClassFQDN = targetClass.getName();
        objectLabel = messageSource.getMessage(targetClassFQDN, null, request.getLocale());
        if (StringUtils.hasLength(objectLabel)) {
            return objectLabel;
        }
        // ラベル取得できなかった場合は、オブジェクト名をそのまま使用
        return objectName;
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
    public Object createWarnErrorResponse(final Exception e, final HttpStatusCode statusCode,
            final WebRequest request) {
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
