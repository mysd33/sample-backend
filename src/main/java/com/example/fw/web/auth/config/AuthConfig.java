package com.example.fw.web.auth.config;

import com.example.fw.web.auth.SpringSecurityUserNameProvider;
import com.example.fw.web.auth.UserIdLogMDCFilter;
import com.example.fw.web.auth.UserNameProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// 認証関連の設定クラス
@Configuration
@ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
public class AuthConfig {

    /// ログにユーザIDを出力するためのFilter登録
    @Bean
    @FilterRegistration
    UserIdLogMDCFilter userIdLogMDCFilter(UserNameProvider userNameProvider) {
        return new UserIdLogMDCFilter(userNameProvider);
    }

    /// Spring Securityの認証情報からユーザ名を取得するUserNameProvider
    @Bean
    UserNameProvider userNameProvider() {
        return new SpringSecurityUserNameProvider();
    }

}
