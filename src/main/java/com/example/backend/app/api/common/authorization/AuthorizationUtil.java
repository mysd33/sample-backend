package com.example.backend.app.api.common.authorization;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

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
        var jwt = (Jwt) authentication.getPrincipal();
        return getUserId(jwt);
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

}
