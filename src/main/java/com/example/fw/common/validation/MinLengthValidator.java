package com.example.fw.common.validation;

import com.example.fw.common.utils.JapaneseStringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 * サロゲートペア対応の最大文字列長チェックを行うValidatorクラス
 *
 */
public class MinLengthValidator implements ConstraintValidator<MinLength, CharSequence> {
    private int length;

    @Override
    public void initialize(final MinLength constraintAnnotation) {
        this.length = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        // 必須入力チェックとして@NotBlankアノテーションと組み合わせて使用することを想定しているため、
        // nullまたは空列、空白の場合はtrueを返す
        if (value == null) {
            return true;
        }
        String valueStr = value.toString(); // CharSequenceがtoString()を実装している前提
        if (valueStr.isBlank()) {
            return true;
        }
        return JapaneseStringUtils.getCodePointLength(valueStr) >= this.length;
    }
}
