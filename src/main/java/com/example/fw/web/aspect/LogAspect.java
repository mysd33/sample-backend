package com.example.fw.web.aspect;

import com.example.fw.web.message.WebFrameworkMessageIds;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;
import com.example.fw.common.logging.ApplicationLogger;
import com.example.fw.common.logging.LoggerFactory;
import com.example.fw.common.logging.MonitoringLogger;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ログ出力クラス Controllerメソッドの開始終了時に情報ログを出力する また、集約例外ハンドリング処理としてエラーログを出力する
 *
 */
@Slf4j
@Aspect
public class LogAspect {
    private static final String LOG_FORMAT_PREFIX = "{0}:";
    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
    private static final MonitoringLogger monitoringLogger = LoggerFactory.getMonitoringLogger(log);

    @Setter
    private String defaultExceptionMessageId;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRestControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.debug("RestController開始：{}", jp.getSignature());
        Object result = jp.proceed();
        appLogger.debug("RestController終了：{}", jp.getSignature());
        return result;
    }

    @Around("@within(org.springframework.stereotype.Controller)")
    public Object aroundControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.debug("Controller開始：{}", jp.getSignature());
        Object result = jp.proceed();
        appLogger.debug("Controller終了：{}", jp.getSignature());
        return result;
    }

    @Around("@within(org.springframework.stereotype.Service)")
    public Object aroundServiceLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.info(WebFrameworkMessageIds.I_ON_FW_0001, jp.getSignature(), Arrays.asList(jp.getArgs()));
        try {
            Object result = jp.proceed();
            appLogger.info(WebFrameworkMessageIds.I_ON_FW_0002, jp.getSignature(), Arrays.asList(jp.getArgs()));
            return result;
        } catch (BusinessException e) {
            String logFormat = new StringBuilder(LOG_FORMAT_PREFIX).append(jp.getSignature()).toString();
            appLogger.warn(e.getCode(), logFormat, e, (Object[]) e.getArgs());
            throw e;
        } catch (SystemException e) {
            String logFormat = new StringBuilder(LOG_FORMAT_PREFIX).append(jp.getSignature()).toString();
            monitoringLogger.error(e.getCode(), logFormat, e, (Object[]) e.getArgs());
            throw e;
        } catch (Exception e) {
            String logFormat = new StringBuilder(LOG_FORMAT_PREFIX).append(jp.getSignature()).toString();
            monitoringLogger.error(defaultExceptionMessageId, logFormat, e);
            throw e;
        }
    }
}
