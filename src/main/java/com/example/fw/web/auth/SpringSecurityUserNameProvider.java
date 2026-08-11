package com.example.fw.web.auth;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

/// Spring Securityを使ったユーザ認証を行う場合のUserNameProvider実装クラス。
public class SpringSecurityUserNameProvider implements UserNameProvider {

    private static final String PREFERRED_USERNAME = "preferred_username";

    @Override
    public @Nullable String getUserName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        var principal = authentication.getPrincipal();
        if (principal == null) {
            return null;
        }
        switch (principal) {
            case UserDetails userDetails -> {
                // 認証済みユーザ情報の場合
                return userDetails.getUsername();
            }
            case Jwt jwt -> {
                // アクセストークンの場合
                var username = jwt.getClaimAsString(PREFERRED_USERNAME);
                return username != null ? username : jwt.getSubject();
            }
            default -> {
                return null;
            }
        }
    }

}
