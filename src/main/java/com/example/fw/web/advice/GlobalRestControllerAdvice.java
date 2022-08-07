package com.example.fw.web.advice;


import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.fw.common.exception.SystemException;
import com.example.fw.common.message.ResultMessage;
import com.example.fw.common.message.ResultMessageType;
import static com.example.fw.common.message.FrameworkMessageIds.E_FW_9001;

/**
 * 
 * 集約例外ハンドリングのためのRestControllerAdviceクラス
 *
 */
@RestControllerAdvice
public class GlobalRestControllerAdvice {
	//TODO:集約例外ハンドリングのブラッシュアップ
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String systemExceptionHandler(final SystemException e, final Model model) {
        // 例外クラスのメッセージをModelに登録
        model.addAttribute(ResultMessage.builder()
        		.type(ResultMessageType.ERROR)
        		.code(e.getCode())
        		.build()
        );
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(final Exception e, final Model model) {

        // 例外クラスのメッセージをModelに登録
        model.addAttribute(ResultMessage.builder()
        		.type(ResultMessageType.ERROR)
        		.code(E_FW_9001)
        		.build()
        );
        
        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        
        return "error";
    }
}
