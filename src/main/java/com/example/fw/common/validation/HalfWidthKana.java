package com.example.fw.common.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.example.fw.common.validation.HalfWidthKana.List;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/// 半角カナ形式の文字列かどうか検証する単項目チェックルールのアノテーション
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@ReportAsSingleViolation
//https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_validation_target_specification_for_purely_composed_constraints
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
// JIS X 0201 半角カナ（org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_SpecialCharsの定義と同じ文字集合）
// をベースに、句読点「｡」（0xFF61）や「､」（0xFF64） 、鍵かっこ'｢'（0xFF62）,や'｣'（0xFF63）は含まず、
// 全角カナのチェックルールと合わせて、長音記号「ｰ」（0xFF70）や中点「･」（0xFF65）は含んだままとして
// 追加で半角スペース（0x20）を許容する正規表現
@Pattern(regexp = "^[･-ﾟ ]*$")

public @interface HalfWidthKana {

    String message() default "{com.example.fw.common.validation.HalfWidthKana.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        HalfWidthKana[] value();
    }
}
