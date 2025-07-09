package com.example.fw.common.validation;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 * 日付形式の文字列かどうか検証するValidatorクラス
 *
 */
public class DateStringValidator implements ConstraintValidator<DateString, CharSequence> {
    // 許容する日付のリストを定義
    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(//
            DateTimeFormatter.ofPattern("yyyy-MM-dd"), //
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), //
            DateTimeFormatter.ofPattern("yyyy/MM/dd"), //
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // nullまたは空列、空白の場合はtrueを返す
        if (value == null || value.toString().isBlank()) {
            return true;
        }
        // 10文字未満の場合は日付形式出ないと判断し、falseを返す
        if (value.length() < 10) {
            return false;
        }
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                // 日付形式に変換できるかどうかを検証
                formatter.parse(value);
                return true;
            } catch (DateTimeParseException e) {
                // 変換できない場合は次のフォーマットを試すため何もしない
            }
        }

        return false;
    }

}
