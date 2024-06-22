package com.example.fw.common.systemdate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import com.example.fw.common.exception.SystemException;
import com.example.fw.common.message.CommonFrameworkMessageIds;

import lombok.RequiredArgsConstructor;

/**
 * 日本のシステム日時を取得する
 * テスト実行時に日付の差し替えができるようになっている
 *
 */
@RequiredArgsConstructor
public class DefaultSystemDate implements SystemDate {
    private static final String ZONE_ID = "Asia/Tokyo";
    // テスト実施時に、コマンドライン引数や環境変数等で、外部からシステム比杖ｋを固定化したい場合に利用
    private final String fixedSystemDate;
    
    @Override
    public ZonedDateTime now() {
        if (fixedSystemDate == null) {       
            return ZonedDateTime.now(ZoneId.of(ZONE_ID));
        } else {
            try {
                return LocalDateTime.parse(fixedSystemDate).atZone(ZoneId.of(ZONE_ID));
            } catch (DateTimeParseException e) {
                throw new SystemException(e, CommonFrameworkMessageIds.E_CM_FW_9002);
            }
        }
    }
}   
