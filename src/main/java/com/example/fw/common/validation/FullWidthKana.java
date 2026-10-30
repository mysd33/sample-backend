package com.example.fw.common.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.example.fw.common.validation.FullWidthKana.List;
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

/// 全角カナ形式の文字列かどうか検証する単項目チェックルールのアノテーション
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@ReportAsSingleViolation
//https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_validation_target_specification_for_purely_composed_constraints
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
// JIS X 0218 5区の全角カナ（org.terasoluna.gfw.common.codepoints.catalog.JIS_X_0208_SpecialCharsの定義と同じ文字集合）
// 全角スペース(0x3000 01-01)、中点「・」(0x30FB 01-06)、長音記号「ー」(0x30FC 01-28)のみを許容する正規表現
@Pattern(regexp = "^[ァ-ヶ\u3000・ー]*$")
public @interface FullWidthKana {

    String message() default "{com.example.fw.common.validation.FullWidthKana.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FullWidthKana[] value();
    }
}
