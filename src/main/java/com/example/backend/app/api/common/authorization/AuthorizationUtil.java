package com.example.backend.app.api.common.authorization;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

/// 認可情報取得ユーティリティクラス
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class AuthorizationUtil {

    /// 認可情報に含まれるユーザIDを取得する
    ///
    /// @return ユーザID
    public static @NonNull String getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "";
        }
        // JWT検証の場合、PrincipalはJwt型になる
        if ((authentication.getPrincipal() instanceof Jwt jwt)) {
            return getUserId(jwt);
        }
        // Opaqueトークン(インストロスペクションエンドポイント)検証の場合、PrincipalはOAuth2IntrospectionAuthenticatedPrincipal型になる
        if (authentication.getPrincipal() instanceof OAuth2IntrospectionAuthenticatedPrincipal principal) {
            return getUserId(principal);
        }

        return "";
    }

    /// アクセストークンからユーザIDを取得する
    ///
    /// @param jwt アクセストークン
    /// @return ユーザID
    public static @NonNull String getUserId(Jwt jwt) {
        if (jwt == null) {
            return "";
        }
        // preferred_nameや独自のクレームを利用する案もあるが、一意性を考慮しsubを利用
        var userId = jwt.getSubject();
        return userId != null ? userId : "";
    }

    /// インストロスペクションエンドポイントの結果からユーザIDを取得する
    ///
    /// @param principal インストロスペクションエンドポイントの結果
    /// @return ユーザID
    public static @NonNull String getUserId(OAuth2IntrospectionAuthenticatedPrincipal principal) {
        if (principal == null) {
            return "";
        }
        // preferred_nameや独自のクレームを利用する案もあるが、一意性を考慮しsubを利用
        var userId = principal.getSubject();
        return userId != null ? userId : "";
    }

}
