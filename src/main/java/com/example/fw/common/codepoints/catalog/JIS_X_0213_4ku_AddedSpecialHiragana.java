package com.example.fw.common.codepoints.catalog;

import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 *  JIS X 0213の追加非漢字の特殊ひらがな（1面4区）の文字集合を表すクラス
 */
public final class JIS_X_0213_4ku_AddedSpecialHiragana extends CodePoints {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public JIS_X_0213_4ku_AddedSpecialHiragana() {
        super(
            0x3094, // ゔ(01-04-84) 
            0x3095, // ゕ(01-04-85) 
            0x3096, // ゖ(01-04-86) 
            0x304B, // か゚(01-04-87) 合成文字
            0x309A, // か゚(01-04-87) 合成文字
            0x304D, // き゚(01-04-88) 合成文字
            0x309A, // き゚(01-04-88) 合成文字
            0x304F, // く゚(01-04-89) 合成文字
            0x309A, // く゚(01-04-89) 合成文字
            0x3051, // け゚(01-04-90) 合成文字
            0x309A, // け゚(01-04-90) 合成文字
            0x3053, // こ゚(01-04-91) 合成文字
            0x309A // こ゚(01-04-91) 合成文字   
        );
    }
}