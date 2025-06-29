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
            0x30fc // ー(01-01-28)    
        );
    }
}