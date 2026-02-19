package com.example.fw.common.codepoints.conversion;

import java.util.Map;

/**
 * JIS X 0213ではない類似の文字を、JIS X 0213の文字のコードポイントに変換するための変換表
 */
public final class CodePointConversionMap {
    // JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）のうち、JIS X 0213ではない類似文字をJIS X 0213の文字に変換するマップ
    private static final Map<Integer, Integer> conversionMap = Map.of(//
            0x2015, 0x2014, // HORIZONTAL BAR（―） → EM DASH(—）
            0xff3c, 0x005c, // FULLWIDTH REVERSE SOLIDUS（＼） → REVERSE SOLIDUS（\）
            0xff5e, 0x301c, // FULLWIDTH TILDE（～） → WAVE DASH（〜）
            0x2225, 0x2016, // PARALLEL TO（∥） → DOUBLE VERTICAL LINE（‖）
            0xff0d, 0x2212, // FULLWIDTH HYPHEN-MINUS（－） → MINUS SIGN（−）
            0xffe0, 0x00a2, // FULLWIDTH CENT SIGN（￠） → CENT SIGN（¢）
            0xffe1, 0x00a3, // FULLWIDTH POUND SIGN（￡） → POUND SIGN（£）
            0xffe2, 0x00ac); // FULLWIDTH NOT SIGN （￢）→ NOT SIGN（¬）

    /**
     * private コンストラクタ
     */
    private CodePointConversionMap() {
    }

    public static Map<Integer, Integer> getMap() {
        return conversionMap;
    }

}
