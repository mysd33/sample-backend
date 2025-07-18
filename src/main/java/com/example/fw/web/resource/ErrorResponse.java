package com.example.fw.web.resource;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST APIのエラーレスポンスクラス
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = -707495429327768166L;

    // エラーコード
    private String code;
    // エラーメッセージ
    private String message;
    // エラーメッセージ詳細
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> details;

}
