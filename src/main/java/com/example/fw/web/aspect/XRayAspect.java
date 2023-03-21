package com.example.fw.web.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;

@Aspect
public class XRayAspect extends BaseAbstractXRayInterceptor {
    /**
     * XRayEnabledアノテーションが付けられたどのクラスをトレースするかポイントカットを定義
     */
    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) " + " && execution(* com.example..*.*(..))")
    protected void xrayEnabledClasses() {
    }

}
