package com.example.fw.common.utils;

/**
 * 日本語の文字列に関するユーティリティクラス
 */
public final class JapaneseStringUtils {
    private JapaneseStringUtils() {    
    }

    /**
     * サロゲートペアを考慮しコードポイントに基づく文字列長を取得する
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
    
}
