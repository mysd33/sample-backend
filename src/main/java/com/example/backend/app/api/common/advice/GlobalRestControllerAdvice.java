package com.example.backend.app.api.common.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.fw.web.advice.AbstractRestControllerAdvice;
import com.example.fw.web.advice.ErrorResponseCreator;

/**
 * 
 * 集約例外ハンドリングのためのRestControllerAdviceクラス
 *
 */
@RestControllerAdvice
public class GlobalRestControllerAdvice extends AbstractRestControllerAdvice {

    public GlobalRestControllerAdvice(ErrorResponseCreator errorResponseCreator) {
        super(errorResponseCreator);
    }


}
