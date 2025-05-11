package com.example.fw.common.systemdate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import com.example.fw.common.exception.SystemException;
import com.example.fw.common.message.CommonFrameworkMessageIds;

import lombok.RequiredArgsConstructor;

/**
 * システム日時を取得する
 * テスト実行時に日付の差し替えができるようになっている<br>
 * 
 * なお、日本の日付にするには、動作環境OSのタイムゾーンが日本時間に設定されている必要がある<br>
 * 例えば、Linuxの場合、TZ環境変数を"Asia/Tokyo"に設定しておく<br>
 *
 */
@RequiredArgsConstructor
public class DefaultSystemDate implements SystemDate {
    // テスト実施時に、コマンドライン引数や環境変数等で、外部からシステム日付を固定化したい場合に利用
    private final String fixedSystemDate;
    
    @Override
    public LocalDateTime now() {
        return nowWithZoneInfo().toLocalDateTime();
    }
    
    @Override
    public ZonedDateTime nowWithZoneInfo() {
        if (fixedSystemDate == null) {       
            return ZonedDateTime.now();
        } else {
            try {
                return LocalDateTime.parse(fixedSystemDate).atZone(ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                throw new SystemException(e, CommonFrameworkMessageIds.E_CM_FW_9002);
            }
        }
    }
}
