package com.example.fw.common.codepoints.catalog;

import com.example.fw.common.codepoints.conversion.CodePointConversionMap;
import java.io.Serial;

import org.terasoluna.gfw.common.codepoints.CodePoints;

/// JIS X 0213の非漢字の特殊文字、記号（1面1区、2区）文字集合の類似文字でWindowsで入力容易な文字としてマッピングされる文字のコードポイント集合
public final class JIS_X_0213_1_2ku_SpecialChars_WindowsMapping extends CodePoints {

    @Serial
    private static final long serialVersionUID = 5814903212987182905L;

    /// コンストラクタ
    public JIS_X_0213_1_2ku_SpecialChars_WindowsMapping() {
        // コード変換表のキー値（変換元のJIS X 0213ではない類似文字のコードポイント）をセットする
        super(CodePointConversionMap.getMap().keySet());
    }
}
