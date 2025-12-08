package com.example.fw.common.codepoints.catalog;

import java.io.Serial;


import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 *  JIS X 0213の追加非漢字の特殊文字、特殊カタカナ（1面7区）の文字集合を表すクラス
 */
public final class JIS_X_0213_7ku_AddedSpecialCharsKatakana extends CodePoints {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public JIS_X_0213_7ku_AddedSpecialCharsKatakana() {
        super(
            0x23be, // ⎾(01-07-34) 
            0x23bf, // ⎿(01-07-35) 
            0x23c0, // ⏀(01-07-36) 
            0x23c1, // ⏁(01-07-37) 
            0x23c2, // ⏂(01-07-38) 
            0x23c3, // ⏃(01-07-39) 
            0x23c4, // ⏄(01-07-40) 
            0x23c5, // ⏅(01-07-41) 
            0x23c6, // ⏆(01-07-42) 
            0x23c7, // ⏇(01-07-43) 
            0x23c8, // ⏈(01-07-44) 
            0x23c9, // ⏉(01-07-45) 
            0x23ca, // ⏊(01-07-46) 
            0x23cb, // ⏋(01-07-47) 
            0x23cc, // ⏌(01-07-48) 
            0x30f7, // ヷ(01-07-82) 
            0x30f8, // ヸ(01-07-83) 
            0x30f9, // ヹ(01-07-84) 
            0x30fa, // ヺ(01-07-85) 
            0x22da, // ⋚(01-07-86) 
            0x22db, // ⋛(01-07-87) 
            0x2153, // ⅓(01-07-88) 
            0x2154, // ⅔(01-07-89) 
            0x2155, // ⅕(01-07-90) 
            0x2713, // ✓(01-07-91) 
            0x2318, // ⌘(01-07-92) 
            0x2423, // ␣(01-07-93) 
            0x23ce // ⏎(01-07-94)    
        );
    }
}