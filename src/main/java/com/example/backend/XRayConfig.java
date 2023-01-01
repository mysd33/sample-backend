package com.example.backend;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;
import com.amazonaws.xray.sql.TracingDataSource;
import com.zaxxer.hikari.HikariDataSource;

@Profile("xray")
@Aspect
@Configuration
public class XRayConfig extends BaseAbstractXRayInterceptor {

	@Override
	@Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) " + " && execution(* com.example..*.*(..))")
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
	
	/**
	 * DataSourceプロパティの取得
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}
	
	/**
	 * AWS X-RayによるJDBCのトレーシング	 
	 */
	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		return TracingDataSource.decorate(
				DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.driverClassName(dataSourceProperties.getDriverClassName())
				.url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword())				
				.build());
	}
}
