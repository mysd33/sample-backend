package com.example.fw.common.codepoints.catalog;

import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 * JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）文字集合でWindowsと他のOSでマッピングが異なる文字
 */
public final class JIS_X_0213_1_2ku_SpecialChars_WindowsMapping extends CodePoints {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public JIS_X_0213_1_2ku_SpecialChars_WindowsMapping() {
        super(0x2015, // ―(01-01-29)
                0xff5e, // ～(01-01-33)
                0x2225, // ∥(01-01-34)
                0xff0d, // －(01-01-61)
                0xff04, // ＄(01-01-80)
                0xffe0, // ￠(01-01-81)
                0xffe2 // ￢(01-02-44)
        );
    }
}