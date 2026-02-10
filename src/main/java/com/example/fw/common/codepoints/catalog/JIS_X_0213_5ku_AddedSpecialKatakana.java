package com.example.fw.common.codepoints.catalog;

import java.io.Serial;


import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 * JIS X 0213の追加非漢字の特殊カタカナ（1面5区）の文字集合を表すクラス
 */
public final class JIS_X_0213_5ku_AddedSpecialKatakana extends CodePoints {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public JIS_X_0213_5ku_AddedSpecialKatakana() {
        super(0x30AB, // カ゚(01-05-87) 合成文字
                0x309A, // カ゚(01-05-87) 合成文字
                0x30AD, // キ゚(01-05-88) 合成文字
                0x309A, // キ゚(01-05-88) 合成文字
                0x30AF, // ク゚(01-05-89) 合成文字
                0x309A, // ク゚(01-05-89) 合成文字
                0x30B1, // ケ゚(01-05-90) 合成文字
                0x309A, // ケ゚(01-05-90) 合成文字
                0x30B3, // コ゚(01-05-91) 合成文字
                0x309A, // コ゚(01-05-91) 合成文字
                0x30BB, // セ゚(01-05-92) 合成文字
                0x309A, // セ゚(01-05-92) 合成文字
                0x30C4, // ツ゚(01-05-93) 合成文字
                0x309A, // ツ゚(01-05-93) 合成文字
                0x30C8, // ト゚(01-05-94) 合成文字
                0x309A // ト゚(01-05-94) 合成文字
        );
    }
}