package com.example.fw.common.codepoints.catalog;

import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 *  特殊文字扱いでも全角文字（記号なし）チェックに含める文字集合を表すクラス
 */
public final class CustomFullWidthCharSet extends CodePoints {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public CustomFullWidthCharSet() {
        super(
            0x3005, // 々(01-01-25) 
            0x3006, // 〆(01-01-26) 
            0x30fc, // ー(01-01-28) 
            0x31f0, // ㇰ(01-06-78) 
            0x31f1, // ㇱ(01-06-79) 
            0x31f2, // ㇲ(01-06-80) 
            0x31f3, // ㇳ(01-06-81) 
            0x31f4, // ㇴ(01-06-82) 
            0x31f5, // ㇵ(01-06-83) 
            0x31f6, // ㇶ(01-06-84) 
            0x31f7, // ㇷ(01-06-85) 
            0x31f8, // ㇸ(01-06-86) 
            0x31f9, // ㇹ(01-06-87) 
            0x31F7, // ㇷ゚(01-06-88) 合成文字
            0x309A, // ㇷ゚(01-06-88) 合成文字
            0x31fa, // ㇺ(01-06-89) 
            0x31fb, // ㇻ(01-06-90) 
            0x31fc, // ㇼ(01-06-91) 
            0x31fd, // ㇽ(01-06-92) 
            0x31fe, // ㇾ(01-06-93) 
            0x31ff, // ㇿ(01-06-94) 
            0x30f7, // ヷ(01-07-82) 
            0x30f8, // ヸ(01-07-83) 
            0x30f9, // ヹ(01-07-84) 
            0x30fa // ヺ(01-07-85)    
        );
    }
}