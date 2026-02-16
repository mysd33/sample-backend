package com.example.fw.common.utils;

import java.util.Map;

/**
 * 日本語の文字列に関するユーティリティクラス
 */
public final class JapaneseStringUtils {
    // JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）のうち、JIS X 0213ではない類似文字をJIS X 0213の文字に変換するマップ
    private static final Map<Integer, Integer> map = Map.of(//
            0x2015, 0x2014, // HORIZONTAL BAR（―） → EM DASH(—）
            0xff3c, 0x005c, // FULLWIDTH REVERSE SOLIDUS（＼） → REVERSE SOLIDUS（\）
            0xff5e, 0x301c, // FULLWIDTH TILDE（～） → WAVE DASH（〜）
            0x2225, 0x2016, // PARALLEL TO（∥） → DOUBLE VERTICAL LINE（‖）
            0xff0d, 0x2212, // FULLWIDTH HYPHEN-MINUS（－） → MINUS SIGN（−）
            0xffe0, 0x00a2, // FULLWIDTH CENT SIGN（￠） → CENT SIGN（¢）
            0xffe1, 0x00a3, // FULLWIDTH POUND SIGN（￡） → POUND SIGN（£）
            0xffe2, 0x00ac); // FULLWIDTH NOT SIGN （￢）→ NOT SIGN（¬）

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
     * JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）文字集合でWindowsと他のOSでマッピングが異なる文字をWindows側の文字に変換する
     * 
     * @param str 対象文字列
     * @return 変換後の文字列
     */
    public static String exchageSpecialChar(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        str.codePoints().forEach(cp -> {
            if (map.containsKey(cp)) {
                sb.appendCodePoint(map.get(cp));
            } else {
                sb.appendCodePoint(cp);
            }
        });
        return sb.toString();
    }

}