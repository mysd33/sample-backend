package com.example.fw.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * 固定長チェック
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
        if (value == null || value.length() == 0) {
            return true;
        }
        return this.length == value.length();
    }
}
