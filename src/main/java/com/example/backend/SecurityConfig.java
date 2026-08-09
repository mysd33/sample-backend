package com.example.backend;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;
import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toStaticResources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/// SpringSecurityの設定クラス
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Spring Securityのデバッグモード
    @Value("${example.security.debug:false}")
    private boolean webSecurityDebug;

    // Basic認証ユーザー設定（application-dev.yml の spring.security.user.* を参照）
    @Value("${spring.security.user.name:user}")
    private String basicAuthUsername;

    @Value("${spring.security.user.password:password}")
    private String basicAuthPassword;

    /// Spring Securityのデバッグモードの設定
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(webSecurityDebug);
    }

    /// Basic認証用のUserDetailsService
    /// spring.security.user.* プロパティをもとにインメモリユーザーを作成する
    /// （Basic認証と、OAuth2の認可を共存させると、
    /// spring-boot-starter-oauth2-resource-serverの存在により
    ///   UserDetailsServiceAutoConfigurationがスキップされるため明示定義が必要）
    @Bean
    UserDetailsService userDetailsService() {
        String password = basicAuthPassword;
        // エンコードプレフィックスがない場合は{noop}を付与
        if (!password.startsWith("{")) {
            password = "{noop}" + password;
        }
        var userDetails = User.withUsername(basicAuthUsername)
            .password(password)
            .roles("USER") // TODO: ロール
            .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    /// Spring Securityによる認証・認可の設定
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // デフォルトの認可設定
        http.authorizeHttpRequests(
            authz -> authz //
                // 静的リソースへアクセス許可
                .requestMatchers(toStaticResources().atCommonLocations()).permitAll()
                // Spring Boot Actuatorのエンドポイントへアクセス許可
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // Springdoc-openapiのドキュメントへ認証なしでアクセス許可
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs*").permitAll()
                // Springdoc-openapiのドキュメントへ認証なしでアクセス許可
                .requestMatchers("/swagger-ui/**").permitAll()
                // Springdoc-openapiのドキュメントへ認証なしでアクセス許可
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated() // それ以外は認証が必要
        );
        return http.build();
    }

    /// Spring SecurityによるOAuth2.0でのAPI認可の設定(v2 api)
    @Bean
    @Order(3)
    @ConditionalOnProperty(name = "example.oidc.enabled", havingValue = "true", matchIfMissing = false)
    SecurityFilterChain securityFilterChainForV2Api(HttpSecurity http) {
        // v2のAPIは、OAuth2.0による認可設定を基本とする
        http.securityMatcher("/api/v2/**")
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            // 認可設定
            .authorizeHttpRequests(
                authz -> authz //
                    // アクセストークンのスコープがtodoのみアクセス可能
                    .requestMatchers("/api/v2/todos/**").hasAnyAuthority("SCOPE_todo")
                    .anyRequest().authenticated() // 認証が必要
            );
        return http.build();
    }

    /// Spring SecurityによるBasic認証でのAPI認可の設定(v1 api)
    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChainForV1Api(HttpSecurity http,
        UserDetailsService userDetailsService) {
        // v1のAPIは、Basic認証による認可設定を基本とする
        http.securityMatcher("/api/v1/**")
            .httpBasic(Customizer.withDefaults())
            // UserDetailsServiceを明示的に設定（Spring Security 7 複数チェーン対応）
            .userDetailsService(userDetailsService)
            // CSRF保護不要
            .csrf(AbstractHttpConfigurer::disable)
            // REST APIはステートレスにする（セッション不使用）
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 認可設定
            .authorizeHttpRequests(
                authz -> authz //
                    .anyRequest().authenticated() // 認証が必要
            );
        return http.build();
    }

    /// H2 Consoleのアクセス許可対応
    @Profile("dev")
    @Order(1)
    @Bean
    SecurityFilterChain securityFilterChainForH2Console(HttpSecurity http) {
        // H2 ConsoleのURLに対して
        http.securityMatcher(toH2Console())//
            .authorizeHttpRequests(
                // 認証不要でアクセス許可
                authz -> authz.anyRequest().permitAll())
            // CSRF保護不要
            .csrf(AbstractHttpConfigurer::disable)
            // H2 Consoleの表示ではframeタグを使用しているのでX-FrameOptionsを無効化
            .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable());
        return http.build();
    }
}
