package com.example.fw.web.servlet.config;

import java.io.IOException;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ログにHTTPリクエストの情報から必要なものをMDCに追加するFilter<br/> 
 * 
 * クラウド・コンテナ上での実行時に、logback-accessでTomcatのアクセスログと通常のログをX-Amzn-Trace-Idで紐づけるために、
 * 通常のログにMDCでX-Amzn-Trace-Idを追加するためのフィルタ
 */
public class LogMDCFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        
        // X-Amzn-Trace-Idヘッダを取得
        String xAmznTraceId = httpRequest.getHeader("X-Amzn-Trace-Id");
        
        if (xAmznTraceId != null) {
            // MDCにX-Amzn-Trace-Idを設定
            MDC.put("x_amzn_trace_id", xAmznTraceId);
        }
        chain.doFilter(request, response);
    }

}
