package com.example.fw.common.validation;

import com.example.fw.common.utils.JapaneseStringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 * サロゲートペア対応の固定文字列長チェックを行うValidatorクラス
 *
 */
public class FixedLengthValidator implements ConstraintValidator<FixedLength, CharSequence> {
    private int length;

    @Override
    public void initialize(final FixedLength constraintAnnotation) {
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
        return JapaneseStringUtils.getCodePointLength(valueStr) == this.length;
    }
}
