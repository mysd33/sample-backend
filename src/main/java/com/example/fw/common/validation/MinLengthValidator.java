package com.example.fw.common.validation;

import com.example.fw.common.utils.JapaneseStringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 * サロゲートペア対応の最大文字列長チェックを行うValidatorクラス
 *
 */
public class MinLengthValidator implements ConstraintValidator<MinLength, String> {
    private int length;

    @Override
    public void initialize(final MinLength constraintAnnotation) {
        this.length = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return JapaneseStringUtils.getCodePointLength(value) >= this.length;
    }
}
