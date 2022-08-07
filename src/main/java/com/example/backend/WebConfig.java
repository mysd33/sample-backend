package com.example.backend;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.backend.domain.message.MessageIds;
import com.example.fw.web.advice.GlobalRestControllerAdvice;
import com.example.fw.web.aspect.LogAspect;

@Configuration
public class WebConfig {
	
	/**
	 * 集約例外ハンドリング機能
	 */
	@Bean
	public GlobalRestControllerAdvice globalRestControllerAdvice(MessageSource messageSource) {
		GlobalRestControllerAdvice advice = new GlobalRestControllerAdvice(messageSource);
		advice.setDefaultExceptionMessageId(MessageIds.E_EX_9001);
		return advice;
	}
	/**
	 * ロギング機能
	 */
	@Bean
	public LogAspect LogAspect() {
		LogAspect logAspect = new LogAspect();
		logAspect.setDefaultExceptionMessageId(MessageIds.E_EX_9001);
		return logAspect;
	}

}
