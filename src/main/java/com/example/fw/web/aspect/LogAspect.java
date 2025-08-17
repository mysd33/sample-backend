package com.example.fw.web.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.exception.DynamoDBTransactionBusinessException;
import com.example.fw.common.exception.SystemException;
import com.example.fw.common.exception.TransactionTimeoutBusinessException;
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
    private static final String LOG_FORMAT_SUFFIX = ", {0}";
    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
    private static final MonitoringLogger monitoringLogger = LoggerFactory.getMonitoringLogger(log);
    private final SystemDate systemDate;
    private final MessageSource messageSource;
    // 入力エラーのログ出力メッセージID
    private final String inputErrorMessageId;
    // 予期せぬエラーのログ出力メッセージID
    private final String unexpectedErrorMessageId;

    /**
     * 水際で、各種エラーに対して適切なログレベルでのログ出力を行う
     */
    @Before("execution(* org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException(..))")
    public void beforeHandlerExceptionResolverLog(final JoinPoint jp) {
        logError((Exception) jp.getArgs()[3]);
    }

    /**
     * 各種エラーに対してのログ出力を行う
     * 
     * @param e 発生した例外
     */
    protected void logError(final Exception e) {
        switch (e) {
        // REST APIでのパスパラメータ、クエリパラメータなどの入力チェックエラーが発生した場合
        case HandlerMethodValidationException hmve -> //
            appLogger.warn(inputErrorMessageId, hmve);
        // REST APIでのリクエストボディのJSONを読み込みResourceオブジェクトを生成する際にエラーが発生した場合
        case HttpMessageNotReadableException hmne -> //
            appLogger.warn(inputErrorMessageId, hmne);
        // REST APIでのリクエストボディのJSONからResourceオブジェクトで入力チェックエラーが発生した場合
        case MethodArgumentNotValidException manve -> //
            appLogger.warn(inputErrorMessageId, manve);
        // REST APIでの入力チェックエラーが発生した場合
        case BindException be -> {
            appLogger.warn(inputErrorMessageId, be);
        }
        // REST API404 Not Foundエラーが発生した場合
        case NoResourceFoundException nrfe -> {
            // 本サンプルAPでは何もしない（案件によってログ追加してもよい）
        }
        case NoHandlerFoundException nhfe -> {
            // 本サンプルAPでは何もしない（案件によってログ追加してもよい）
        }
        // REST API405 Method Not Allowedエラーが発生した場合
        case HttpRequestMethodNotSupportedException hrmse -> {
            // 本サンプルAPでは何もしない（案件によってログ追加してもよい）
        }
        // 業務エラー（RestControllerで発生するの専用業務例外）の場合（案件によって例外の追加が必要）
        // DynamoDBトランザクションで条件付き更新などに失敗した場合に業務エラーで扱うための業務例外
        case DynamoDBTransactionBusinessException ddbtbe -> //
            // Serviceでログ出力されない業務例外なので、ここでログ出力する
            appLogger.warn(ddbtbe.getCode(), ddbtbe, (Object[]) ddbtbe.getArgs());
        // RDBトランザクションタイムアウト時に業務エラーとして扱うための業務例外
        case TransactionTimeoutBusinessException ttbe -> //
            // Serviceでログ出力されない業務例外なので、ここでログ出力する
            appLogger.warn(ttbe.getCode(), ttbe, (Object[]) ttbe.getArgs());
        // 業務エラー（Serviceで発生する業務例外）の場合
        case BusinessException be -> {
            // Serviceでログ出力するので、二重でスタックトーレス含むログを出力しないよう何もしない
        }
        // システムエラー（システム例外）の場合
        case SystemException se -> //
            monitoringLogger.error(se.getCode(), se, (Object[]) se.getArgs());
        // システムエラー（予期せぬ例外）の場合
        case null, default -> //
            monitoringLogger.error(unexpectedErrorMessageId, e);        
        }
    }

    /**
     * Controllerのメソッド開始・終了時に業務トレースログを出力する
     */
    @Around("@within(org.springframework.stereotype.Controller)")
    public Object aroundControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        // ログ解析を容易にするため、処理開始日時をログ出力するために取得
        LocalDateTime startDateTime = systemDate.now();
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0001, jp.getSignature(), startDateTime);
        try {
            Object result = jp.proceed();
            // 処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0002, jp.getSignature(), elapsedTime, startDateTime);
            return result;
        } catch (Exception e) {
            // 処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            Object[] args = { jp.getSignature(), elapsedTime, startDateTime };
            String message = messageSource.getMessage(WebFrameworkMessageIds.W_FW_ONCTRL_8001, args, Locale.getDefault());
            String logFormat = message + LOG_FORMAT_SUFFIX;
            switch (e) {
            // ここでは、メソッドが異常終了した旨を警告ログのみ出力
            // WebブラウザAPの場合、通常業務例外はControllerのメソッド内でキャッチされるが、
            // 万が一発生した場合は、業務例外のコードと引数をログ出力する
            case BusinessException be -> appLogger.warn(be.getCode(), logFormat, null, (Object[]) be.getArgs());
            case SystemException se -> appLogger.warn(se.getCode(), logFormat, null, (Object[]) se.getArgs());
            default -> appLogger.warn(unexpectedErrorMessageId, logFormat, null);
            }
            throw e;
        }
    }

    /**
     * RestControllerのメソッド開始・終了時に業務トレースログを出力する
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRestControllerLog(final ProceedingJoinPoint jp) throws Throwable {
        // ログ解析を容易にするため、処理開始日時をログ出力するために取得
        LocalDateTime startDateTime = systemDate.now();
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0003, jp.getSignature(), startDateTime);
        try {
            Object result = jp.proceed();
            // 処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0004, jp.getSignature(), elapsedTime, startDateTime);
            return result;
        } catch (Exception e) {
            // 処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            Object[] args = { jp.getSignature(), elapsedTime, startDateTime };
            String message = messageSource.getMessage(WebFrameworkMessageIds.W_FW_ONCTRL_8002, args, Locale.getDefault());
            String logFormat = message + LOG_FORMAT_SUFFIX;
            switch (e) {
            // 業務エラーは、ここでは、メソッドが異常終了した旨を警告ログのみ出力。スタックトレースは出力しない
            case BusinessException be -> //
                appLogger.warn(be.getCode(), logFormat, null, (Object[]) be.getArgs());
            // システムエラーは、ここではメソッドが異常終了した旨を警告ログのみ出力。スタックトレースは出力しない
            case SystemException se -> //
                appLogger.warn(se.getCode(), logFormat, null, (Object[]) se.getArgs());
            default -> appLogger.warn(unexpectedErrorMessageId, logFormat, null);
            }
            throw e;
        }
    }

    /**
     * Serviceのメソッド開始・終了時に業務トレースログを出力する
     */
    @Around("@within(org.springframework.stereotype.Service)")
    public Object aroundServiceLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0005, jp.getSignature(), Arrays.asList(jp.getArgs()));
        try {
            Object result = jp.proceed();
            appLogger.info(WebFrameworkMessageIds.I_FW_ONCTRL_0006, jp.getSignature(), Arrays.asList(jp.getArgs()));
            return result;
        } catch (Exception e) {
            // 例外が発生した場合は、エラーログを出力
            Object[] args = { jp.getSignature(), Arrays.asList(jp.getArgs()) };
            String message = messageSource.getMessage(WebFrameworkMessageIds.W_FW_ONCTRL_8003, args, Locale.getDefault());
            String logFormat = message + LOG_FORMAT_SUFFIX;
            switch (e) {
            // 業務エラーは、メソッドが異常終了した旨を警告ログを出力するが、
            // WebブラウザAPのケースを考慮すると、通常業務例外はControllerのメソッド内でキャッチされ、
            // 警告ログがでないため、ここでスタックトレース含めて出力する。
            case BusinessException be -> //
                appLogger.warn(be.getCode(), logFormat, e, (Object[]) be.getArgs());
            // システムエラーは、ここではメソッドが異常終了した旨を警告ログのみ出力。スタックトレースは出力しない。
            case SystemException se -> //
                appLogger.warn(se.getCode(), logFormat, null, (Object[]) se.getArgs());
            default -> appLogger.warn(unexpectedErrorMessageId, logFormat, null);
            }
            throw e;
        }
    }

    /**
     * Repositoryのメソッド開始・終了時に性能ログを出力する
     */
    @Around("@within(org.springframework.stereotype.Repository)")
    public Object aroundRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        return doAroundRepositoryLog(jp);
    }

    /**
     * Repository（MyBatisのMapperインタフェース）のメソッド開始・終了時に性能ログ出力する
     */
    @Around("@within(org.apache.ibatis.annotations.Mapper)")
    public Object aroundMybatisMapperRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        return doAroundRepositoryLog(jp);
    }

    /**
     * DynamoDBのトランザクションコミット時（実際にRepositoryの操作が実行されるとき）に性能ログを出力する
     */
    @Around("execution(* com.example.fw.common.dynamodb.DynamoDBTransactionManager.commit(..))")
    public Object aroundDynamoDBTransactionCommitLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.trace(WebFrameworkMessageIds.T_FW_ONCTRL_0003, jp.getSignature(), Arrays.asList(jp.getArgs()));
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        try {
            return jp.proceed();
        } finally {
            // 呼び出し処理実行後、処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.trace(WebFrameworkMessageIds.T_FW_ONCTRL_0004, //
                    jp.getSignature(), Arrays.asList(jp.getArgs()), elapsedTime);
        }
    }

    private Object doAroundRepositoryLog(final ProceedingJoinPoint jp) throws Throwable {
        appLogger.trace(WebFrameworkMessageIds.T_FW_ONCTRL_0001, jp.getSignature(), Arrays.asList(jp.getArgs()));
        // 処理時間を計測しログ出力
        long startTime = System.nanoTime();
        try {
            return jp.proceed();
        } finally {
            // 呼び出し処理実行後、処理時間を計測しログ出力
            long endTime = System.nanoTime();
            double elapsedTime = SystemDateUtils.calcElapsedTimeByMilliSeconds(startTime, endTime);
            appLogger.trace(WebFrameworkMessageIds.T_FW_ONCTRL_0002, //
                    jp.getSignature(), Arrays.asList(jp.getArgs()), elapsedTime);
        }
    }

}
