package com.example.fw.common.validation;

import com.example.fw.common.utils.JapaneseStringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 * サロゲートペア対応の最大文字列長チェックを行うValidatorクラス
 *
 */
public class MaxLengthValidator implements ConstraintValidator<MaxLength, CharSequence> {
    private int length;

    @Override
    public void initialize(final MaxLength constraintAnnotation) {
        this.length = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String valueStr = value.toString();
        if (valueStr.isEmpty()) {
            return true;
        }
        return JapaneseStringUtils.getCodePointLength(valueStr) <= this.length;
    }
}
