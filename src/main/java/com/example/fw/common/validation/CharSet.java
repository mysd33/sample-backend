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
import org.terasoluna.gfw.common.codepoints.catalog.ASCIIControlChars;
import org.terasoluna.gfw.common.codepoints.catalog.ASCIIPrintableChars;
import org.terasoluna.gfw.common.codepoints.catalog.CRLF;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0201_Katakana;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0201_LatinLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_BoxDrawingChars;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_CyrillicLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_GreekLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_Hiragana;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_Katakana;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_LatinLetters;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_SpecialChars;
import org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0213_Kanji;

import com.example.fw.common.codepoints.catalog.JIS_X_0213_12_13ku_AddedSpecialChars;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_1_2ku_SpecialChars_WindowsMapping;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_2_3ku_AddedSpecialChars;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_4ku_AddedSpecialHiragana;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_5ku_AddedSpecialKatakana;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_6ku_AddedSpecialCharsKatakana;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_7ku_AddedSpecialCharsKatakana;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_8ku_AddedSpecialChars;
import com.example.fw.common.codepoints.catalog.JIS_X_0213_9_11ku_AddedSpecialChars;
import com.example.fw.common.validation.CharSet.List;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * 本システムが許容する文字集合を検証する単項目チェックルールのアノテーション
 */
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@ReportAsSingleViolation
//https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_validation_target_specification_for_purely_composed_constraints
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
// 実際のシステムの文字集合の範囲に応じて調整すること
@ConsistOf({ //
        ASCIIControlChars.class, // ASCII制御文字の集合
        ASCIIPrintableChars.class, // ASCII印字可能文字の集合
        CRLF.class, // 改行コードの集合
        JIS_X_0201_Katakana.class, // JIS X 0201 の半角カタカナの集合
        JIS_X_0201_LatinLetters.class, // JIS X 0201 のラテン文字の集合
        JIS_X_0208_SpecialChars.class, // JIS X 0208の1-2区：全角特殊文字の集合
        JIS_X_0208_LatinLetters.class, // JIS X 0208 の3区：全角英数字の集合
        JIS_X_0208_Hiragana.class, // JIS X 0208 の4区：ひらがなの集合
        JIS_X_0208_Katakana.class, // JIS X 0208 の5区：全角カタカナの集合
        JIS_X_0208_GreekLetters.class, // JIS X 0208 の6区：ギリシャ文字の集合
        JIS_X_0208_CyrillicLetters.class, // JIS X 0208 の7区：キリル文字の集合
        JIS_X_0208_BoxDrawingChars.class, // JIS X 0208 の8区：罫線素片の集合
        JIS_X_0213_Kanji.class, // JIS第1～4水準の漢字の集合
        JIS_X_0213_1_2ku_SpecialChars_WindowsMapping.class, // JIS X0218の1-2区でWindowsと他のOSでマッピングが異なる文字の集合
        JIS_X_0213_2_3ku_AddedSpecialChars.class, // JIS X 0213 第2～3区の追加特殊文字、記号の集合
        JIS_X_0213_4ku_AddedSpecialHiragana.class, // JIS X 0213 第4区の追加特殊ひらがなの集合
        JIS_X_0213_5ku_AddedSpecialKatakana.class, // JIS X 0213 第5区の追加特殊カタカナの集合
        JIS_X_0213_6ku_AddedSpecialCharsKatakana.class, // JIS X 0213 第6区の追加特殊文字、特殊カタカナの集合
        JIS_X_0213_7ku_AddedSpecialCharsKatakana.class, // JIS X 0213 第7区の追加特殊文字、特殊カタカナの集合
        JIS_X_0213_8ku_AddedSpecialChars.class, // JIS X 0213 第8区の追加特殊文字、記号の集合
        JIS_X_0213_9_11ku_AddedSpecialChars.class, // JIS X 0213 第9～11区の追加特殊文字、記号の集合
        JIS_X_0213_12_13ku_AddedSpecialChars.class, // JIS X 0213 第12～13区の追加特殊文字、記号の集合
})
public @interface CharSet {
    String message() default "{com.example.fw.common.validation.CharSet.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CharSet[] value();
    }
}
