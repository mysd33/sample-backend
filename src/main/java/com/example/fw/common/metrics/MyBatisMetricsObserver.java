package com.example.fw.common.metrics;

import java.lang.reflect.InvocationTargetException;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

/**
 * Spring Boot Actuator/MicrometerのカスタムメトリクスとしてMyBatisに関するメトリクスを観測するクラス
 */
// MyBatisのプラグイン機能を使用し、Executorのupdate、query、queryCursorメソッドをインターセプトしてメトリクスを取得
// https://mybatis.org/mybatis-3/ja/configuration.html#plugins
@Intercepts({ @Signature(type = Executor.class, //
        method = "update", //
        args = { MappedStatement.class, Object.class }), //
        @Signature(type = Executor.class, //
                method = "query", //
                args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }), //
        @Signature(type = Executor.class, //
                method = "query", //
                args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
                        BoundSql.class }) //
        , @Signature(type = Executor.class, //
                method = "queryCursor", //
                args = { MappedStatement.class, Object.class, RowBounds.class }), //
})
@RequiredArgsConstructor
public class MyBatisMetricsObserver implements Interceptor {
    private static final String METER_NAME_PREFIX = "mybatis.query";
    private static final String ID = "id";
    private static final String TYPE = "type";
    private final ObservationRegistry observationRegistry;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 第1引数のMappedStatementを取得し、SQLのIDとコマンドタイプを取得する
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // Micrometer Observationを使用してメトリクスを観測する
        // https://docs.spring.io/spring-boot/reference/actuator/observability.html
        // https://docs.micrometer.io/micrometer/reference/observation/introduction.html
        try {
            return Observation.createNotStarted(METER_NAME_PREFIX, observationRegistry)
                    .lowCardinalityKeyValue(ID, mappedStatement.getId())
                    .lowCardinalityKeyValue(TYPE, mappedStatement.getSqlCommandType().name())//
                    .observe(() -> {
                        try {
                            // 実際のメソッドを呼び出す
                            return invocation.proceed();
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            // いったん非検査例外でラップして再スローする
                            throw new PluginException(e);
                        }
                    });
        } catch (PluginException e) {
            // PluginExceptionから再度元の例外を取得して再スローする
            Throwable cause = e.getCause();
            if (cause != null) {
                throw cause;
            }
            throw e;
        }
    }

}
