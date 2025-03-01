package com.example.fw.web.advice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.NamingBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRestControllerAdvice extends ResponseEntityExceptionHandler {
    protected final ErrorResponseCreator errorResponseCreator;

    // ObjectMapperのPropertyNamingStrategyを取得するためのフィールド
    private ObjectMapper objectMapper;
    // 業務のRestControllerAdviceクラスのコンストラクタが煩雑にならないようSetterインジェクションを利用
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 入力エラーのハンドリング （MethodArgumentNotValidException）
     * リクエストBODYに指定されたJSONに対する入力チェックでエラーが発生した場合
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return handleBindingResult(ex, ex.getBindingResult(), headers, statusCode, request);
    }
    
    /**
     * 入力エラー時のBindingResultを元にエラーレスポンスを作成する
     * @param ex 例外
     * @param bindingResult BindingResult
     * @param headers　Httpヘッダー
     * @param statusCode Httpステータスコード
     * @param request WebRequest
     * @return エラーレスポンス
     */
    private ResponseEntity<Object> handleBindingResult(Exception ex, BindingResult bindingResult, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        Object body = errorResponseCreator.createValidationErrorResponse(bindingResult, request);

        return handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /**
     * 入力エラーのハンドリング （HttpMessageNotReadableException）
     * JSONからResourceオブジェクトを生成する際にエラーが発生した場合
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        // リソースのフォーマットとしてJSONを使用する場合、HttpMessageNotReadableExceptionの原因例外として格納されるものをハンドリング
        // (参考) https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/WebServiceDetail/REST.html#resthowtouseexceptionhandlingforvalidationerror
        // なお、Resourceオブジェクトに存在しないフィールドがJSONに指定されてUnrecognizedPropertyExceptionがスローされるが
        // JsonMappingExceptionのサブクラスであるため、JsonParseException、JsonMappingExceptionの２つをハンドリングする
        // また、Spring Bootの場合、デフォルトでは、ObjectMapperのDeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIESがfalseで作成されるため
        // UnrecognizedPropertyExceptionはスローされない
        // spring.jackson.deserialization.fail-on-unknown-properties=trueをapplication.yamlに設定することで、例外発生する。
        if (ex.getCause() instanceof JsonParseException cause) {
            // JSONとして不正な構文の場合
            Object body = errorResponseCreator.createRequestFormatErrorResponse(cause, request);
            return handleExceptionInternal(ex, body, headers, statusCode, request);
        } else if (ex.getCause() instanceof JsonMappingException cause) {
            // JSONからResourceオブジェクトへ変換する際に、値の型変換またはエラーが発生した場合、
            // もしくは、spring.jackson.deserialization.fail-on-unknown-properties=trueの場合に、
            // Resourceオブジェクトに存在しないフィールドがJSONに指定された場合に、エラーの原因となったフィールドを抽出
            List<InvalidFormatField> fields = new ArrayList<>();
            cause.getPath().forEach(ref -> {
                Class<?> fromClass = ref.getFrom().getClass();
                String jsonFieldName = ref.getFieldName();
                String propertyDescription = getPropertyDescription(fromClass, jsonFieldName);
                if (StringUtils.hasLength(propertyDescription)) {
                    fields.add(InvalidFormatField.builder().fieldName(jsonFieldName).description(propertyDescription)
                            .build());
                } else {
                    fields.add(InvalidFormatField.builder().fieldName(jsonFieldName).build());
                }
            });
            Object body = errorResponseCreator.createRequestMappingErrorResponse(fields, cause, request);
            return handleExceptionInternal(ex, body, headers, statusCode, request);
        } else {
            // その他の例外は想定外のため、警告エラーとしてハンドリングする
            Object body = errorResponseCreator.createWarnErrorResponse(ex, statusCode, request);
            return handleExceptionInternal(ex, body, headers, statusCode, request);
        }
    }

    /**
     * プロパティに関するDescriptionアノテーションの値を取得する
     * 
     * @param clazz         対象のクラス
     * @param jsonFieldName Jsonのフィールド名
     * @return JsonPropertyDescriptionアノテーションの値
     */
    private String getPropertyDescription(Class<?> clazz, String jsonFieldName) {
        List<Field> fields = List.of(clazz.getDeclaredFields());
        for (Field field : fields) {
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            // @JsonPropertyが付与されている場合はその値を優先して使用、
            // 付与されていない場合はPropertyNamingStrategyで変換した値を使用する
            String fieldName = jsonProperty != null ? jsonProperty.value()
                    : ((NamingBase) objectMapper.getPropertyNamingStrategy()).translate(field.getName());
            if (!fieldName.equals(jsonFieldName)) {
                continue;
            }
            // フィルード名が一致する場合に、@JsonPropertyDescriptionがあればその値を返却
            JsonPropertyDescription jsonPropertyDescription = field.getAnnotation(JsonPropertyDescription.class);
            if (jsonPropertyDescription != null) {
                return jsonPropertyDescription.value();
            }
            // @Schemaがあれば、そのdescription属性を返却
            Schema schema = field.getAnnotation(Schema.class);
            if (schema != null) {
                return schema.description();
            }

            return null;
        }
        return null;
    }

    /**
     * 404 NotFoundを警告エラーとしてハンドリング
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        Object body = errorResponseCreator.createWarnErrorResponse(ex, statusCode, request);
        return handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /**
     * 405 Method Not Allowedを警告エラーとしてハンドリング
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Object body = errorResponseCreator.createWarnErrorResponse(ex, statusCode, request);
        return handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /**
     * 業務エラーのハンドリング
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> bussinessExceptionHandler(final BusinessException e, final WebRequest request) {
        Object body = errorResponseCreator.createBusinessErrorResponse(e, request);
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * システムエラーのハンドリング
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> systemExceptionHandler(final SystemException e, final WebRequest request) {
        Object body = errorResponseCreator.createSystemErrorResponse(e, request);
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * システムエラー（予期せぬ例外）のハンドリング
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(final Exception e, final WebRequest request) {
        Object body = errorResponseCreator.createUnexpectedErrorResponse(e, request);
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}