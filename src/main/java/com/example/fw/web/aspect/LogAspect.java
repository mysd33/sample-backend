package com.example.fw.web.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.SystemException;
import com.example.fw.common.logging.ApplicationLogger;
import com.example.fw.common.logging.LoggerFactory;
import com.example.fw.common.logging.MonitoringLogger;
import com.example.fw.common.systemdate.SystemDate;
import com.example.fw.common.systemdate.SystemDateUtils;
import com.example.fw.web.message.WebFrameworkMessageIds;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ログ出力クラス Controllerメソッドの開始終了時に情報ログを出力する また、集約例外ハンドリング処理としてエラーログを出力する
 *
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogAspect {
    private final SystemDate systemDate;
    private static final String LOG_FORMAT_PREFIX = "{0}:";
    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
    private static final MonitoringLogger monitoringLogger = LoggerFactory.getMonitoringLogger(log);

    private final String defaultExceptionMessageId;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRestControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        LocalDateTime startDateTime = systemDate.now();
        appLogger.info(WebFrameworkMessageIds.I_ON_FW_0006, jp.getSignature(), startDateTime);
        try {
            return jp.proceed();
        } finally {
            // 処理時間を計測しログ出力
            LocalDateTime endDateTime = systemDate.now();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startDateTime, endDateTime);
            appLogger.info(WebFrameworkMessageIds.I_ON_FW_0007, jp.getSignature(), elapsedTime, startDateTime);
        }
    }

    @Around("@within(org.springframework.stereotype.Controller)")
    public Object aroundControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        LocalDateTime startDateTime = systemDate.now();
        appLogger.info(WebFrameworkMessageIds.I_ON_FW_0004, jp.getSignature(), startDateTime);
        try {
            return jp.proceed();
        } finally {
            // 処理時間を計測しログ出力
            LocalDateTime endDateTime = systemDate.now();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startDateTime, endDateTime);
            appLogger.info(WebFrameworkMessageIds.I_ON_FW_0005, jp.getSignature(), elapsedTime, startDateTime);
        }
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

    @Around("@within(org.springframework.stereotype.Repository)")
    public Object aroundRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        return doAroundRepositoryLog(jp);
    }

    @Around("@within(org.apache.ibatis.annotations.Mapper)")
    public Object aroundMybatisMapperRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        return doAroundRepositoryLog(jp);
    }

    @Around("execution(* com.example.fw.common.dynamodb.DynamoDBTransactionManager.commit(..))")
    public Object aroundDynamoDBTransactionCommitLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.trace(WebFrameworkMessageIds.T_ON_FW_0003, jp.getSignature(), Arrays.asList(jp.getArgs()));
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        try {
            return jp.proceed();
        } finally {
            // 呼び出し処理実行後、処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.trace(WebFrameworkMessageIds.T_ON_FW_0004, //
                    jp.getSignature(), Arrays.asList(jp.getArgs()), elapsedTime);
        }
    }

    private Object doAroundRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.trace(WebFrameworkMessageIds.T_ON_FW_0001, jp.getSignature(), Arrays.asList(jp.getArgs()));
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        try {
            return jp.proceed();
        } finally {
            // 呼び出し処理実行後、処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.trace(WebFrameworkMessageIds.T_ON_FW_0002, //
                    jp.getSignature(), Arrays.asList(jp.getArgs()), elapsedTime);
        }
    }

}
