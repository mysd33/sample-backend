package com.example.fw.common.utils;

import java.util.Map;

import org.terasoluna.gfw.common.fullhalf.DefaultFullHalf;

import com.example.fw.common.codepoints.conversion.CodePointConversionMap;

/**
 * 日本語の文字列に関するユーティリティクラス
 */
public final class JapaneseStringUtils {
    /**
     * コード変換のマッピング表
     */
    private static final Map<Integer, Integer> conversionMap = CodePointConversionMap.getMap();

    private JapaneseStringUtils() {
    }

    /**
     * サロゲートペアを考慮しコードポイントに基づく文字列長を取得する
     * 
     * @param str 対象文字列
     * @return 文字列長
     */
    public static int getCodePointLength(String str) {
        if (str == null) {
            return 0;
        }
        // https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/GeneralFuncDetail/StringProcessing.html#stringprocessinghowtogetsurrogatepairstringlength
        return str.codePointCount(0, str.length());
    }

    /**
     * 半角文字列を全角文字列に変換する
     */
    public static String toFullwidth(String str) {
        if (str == null) {
            return null;
        }
        return DefaultFullHalf.INSTANCE.toFullwidth(str);
    }

    /**
     * 全角文字列を半角文字列に変換する
     */
    public static String toHalfwidth(String str) {
        if (str == null) {
            return null;
        }
        return DefaultFullHalf.INSTANCE.toHalfwidth(str);
    }

    /**
     * JIS X 0213ではない類似の文字をJIS X 0213の文字のコードポイントに変換する<br>
     * JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）のうち、JIS X 0213ではない類似文字をJIS X 0213の文字に変換する
     * 
     * @param str 対象文字列
     * @return 変換後の文字列
     */
    public static String convertSpecialChar(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        str.codePoints().forEach(cp -> {
            // 変換表にあれば変換
            if (conversionMap.containsKey(cp)) {
                sb.appendCodePoint(conversionMap.get(cp));
            } else {
                sb.appendCodePoint(cp);
            }
        });
        return sb.toString();
    }

}