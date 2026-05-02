package com.example.fw.web.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;

/**
 * X-Ray SDKを用いた場合のAspectクラス。
 * 
 * @deprecated X-Ray SDKは 2027 年 2 月 25 日にサポート終了となるため削除予定
 */
@Deprecated(forRemoval = true)
@Aspect
public class XRayAspect extends BaseAbstractXRayInterceptor {

    /**
     * XRayEnabledアノテーションが付けられたどのクラスをトレースするかポイントカットを定義
     * 
     * @deprecated X-Ray SDKは 2027 年 2 月 25 日にサポート終了となるため削除予定
     */
    @Deprecated(forRemoval = true)
    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) " + " && execution(* com.example..*.*(..))")
    protected void xrayEnabledClasses() {
    }

}
