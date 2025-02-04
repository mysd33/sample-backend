package com.example.fw.web.servlet.logback;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.access.common.pattern.AccessConverter;
import ch.qos.logback.access.common.spi.IAccessEvent;

/**
 * セッションIDをハッシュ化して出力する{@link AccessConverter}。<br>
 * 
 * セッションIDが平文でログに出力されると、セキュリティ上好ましくないことから、セッションIDをハッシュ化して出力する。<br>
 * 
 */
public class HashedSessionIdConverter extends AccessConverter {
    // ハッシュアルゴリズム
    private static final String SHA_256 = "SHA-256";
    // Spring Session利用の場合は、CookieよりSESSIONを取得する必要がある
    private static final String SESSION = "SESSION";
    // ハッシュ化したものをHEX形式で出力するためのフォーマットで、スレッドセーフ
    private static final HexFormat HEX_FORMAT = HexFormat.of();
    // MessageDigestはスレッドセーフでないため、ThreadLocalを利用してスレッド毎にインストールを保持する。
    private static final ThreadLocal<MessageDigest> DIGEST = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    });

    @Override
    public String convert(IAccessEvent event) {
        // JSESSIONIDを取得
        String sessionId = event.getSessionID();
        if (DIGEST.get() == null) {
            return sessionId;
        }
        if (StringUtils.isNotEmpty(sessionId) && !IAccessEvent.NA.equals(sessionId)) {
            return hash(sessionId);
        }
        // JSESSIONIDを取得できない場合は、Spring Session利用の場合を考慮して
        // CookieよりSESSIONを取得する
        sessionId = event.getCookie(SESSION);
        if (StringUtils.isNotEmpty(sessionId) && !IAccessEvent.NA.equals(sessionId)) {
            return hash(sessionId);
        }
        return sessionId;
    }

    @Override
    public void stop() {
        super.stop();
        DIGEST.remove();
    }

    // ハッシュ化してHEX形式で出力する
    private String hash(String sessionId) {
        byte[] hash = DIGEST.get().digest(sessionId.getBytes(StandardCharsets.UTF_8));
        return HEX_FORMAT.formatHex(hash);
    }
}
