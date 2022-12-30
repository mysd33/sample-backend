package com.example.backend;

import javax.servlet.Filter;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;

@Aspect
@Configuration
public class XRayConfig extends BaseAbstractXRayInterceptor {
	
	@Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) " +
            " && execution(* com.example..*.*(..))" )
	protected void xrayEnabledClasses() {		
	}
	
	
	/**
	 * AWS X-Rayによる分散トレーシングの設定
	 * 
	 */
	@Bean
	public Filter tracingFilter() {
		return new AWSXRayServletFilter("sample-backend");
	}
		
}
