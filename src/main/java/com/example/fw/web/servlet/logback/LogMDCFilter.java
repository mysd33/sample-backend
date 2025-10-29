package com.example.fw.web.servlet.logback;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ログにHTTPリクエストの情報から必要なものをMDCに追加するFilter<br/>
 * 
 * クラウド・コンテナ上での実行時に、logback-accessでTomcatのアクセスログと通常のログをX-Amzn-Trace-Idで紐づけるために、
 * 通常のログにMDCでX-Amzn-Trace-Idを追加するためのフィルタ
 */
public class LogMDCFilter extends OncePerRequestFilter {
    private static final String X_AMZN_TRACE_ID = "X-Amzn-Trace-Id";
    private static final String MDC_X_AMZN_TRACE_ID_NAME = "x_amzn_trace_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // X-Amzn-Trace-Idヘッダを取得
        String xAmznTraceId = request.getHeader(X_AMZN_TRACE_ID);

        try {
            if (xAmznTraceId != null) {
                // MDCにX-Amzn-Trace-Idを設定
                MDC.put(MDC_X_AMZN_TRACE_ID_NAME, xAmznTraceId);
            }
            filterChain.doFilter(request, response);
        } finally {
            // 処理終了後にMDCから削除
            if (xAmznTraceId != null) {
                MDC.remove(MDC_X_AMZN_TRACE_ID_NAME);
            }
        }
    }

}
