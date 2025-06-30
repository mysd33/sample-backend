package com.example.fw.common.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.terasoluna.gfw.common.codepoints.ConsistOf;
import org.terasoluna.gfw.common.codepoints.catalog.CRLF;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_CyrillicLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_GreekLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_Hiragana;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_Katakana;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_LatinLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0213_Kanji;

import com.example.fw.common.codepoints.catalog.CustomFullWidthCharSet;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_4ku_AddedSpecialHiragana;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_5ku_AddedSpecialKatakana;
import com.example.fw.common.validation.FullWidthNoSymbol.List;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

/**
 * 全角文字列（記号なし）かどうか検証する単項目チェックルールのアノテーション
 */
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@ReportAsSingleViolation
// 実際のシステムの全角文字（記号なし）の範囲に応じて調整すること
@ConsistOf({ //
        CRLF.class, // 改行コード
        // JIS X 0208 の1-2区：特殊文字は除外
        JIS_X_0208_LatinLetters.class, // JIS X 0208 の3区：全角英数字の集合
        JIS_X_0208_Hiragana.class, // JIS X 0208 の4区：ひらがなの集合
        JIS_X_0208_Katakana.class, // JIS X 0208 の5区：全角カタカナの集合
        JIS_X_0208_GreekLetters.class, // JIS X 0208 の6区：ギリシャ文字の集合
        JIS_X_0208_CyrillicLetters.class, // JIS X 0208 の7区：キリル文字の集合
        // JIS X 0208 の8区：罫線素片は除外
        JIS_X_0213_Kanji.class, // JIS第1～4水準の漢字の集合
        JIS_X_0213_4ku_AddedSpecialHiragana.class, // JIS X 0213 第4区の追加特殊ひらがなの集合
        JIS_X_0213_5ku_AddedSpecialKatakana.class, // JIS X 0213 第5区の追加特殊カタカナの集合
        CustomFullWidthCharSet.class // 上記以外で独自に追加定義する全角文字の集合
})
public @interface FullWidthNoSymbol {
    String message() default "{com.example.fw.common.validation.FullWidth.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FullWidthNoSymbol[] value();
    }
}
