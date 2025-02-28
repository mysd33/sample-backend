package com.example.fw.common.systemdate;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.concurrent.TimeUnit;

/**
 * システム日時に関するユーティリティクラス
 */
public final class SystemDateUtils {
	private SystemDateUtils() {
	}

	/**
	 * ZonedLocalDateTime等の単位に対して、処理時間（ミリ秒小数点第三位の精度）を計算する
	 * 
	 * @param startDateTime 開始時のシステム日時
	 * @param endDateTime   終了時のシステム日時
	 * @return 処理時間（ミリ秒小数点第三位の精度）
	 */
	public static double calcElapsedTimeByMilliSeconds(Temporal startDateTime, Temporal endDateTime) {
		long durationByNanoUnit = Duration.between(startDateTime, endDateTime).toNanos();
		// ナノ秒をマイクロ秒の単位に変換後、小数点第三位の精度を持つミリ秒の表現に変換
		return TimeUnit.NANOSECONDS.toMicros(durationByNanoUnit) / 1000d;
	}

	/**
	 * Unix時間に対して、処理時間（ミリ秒小数点第三位の精度）を計算する
	 * 
	 * @param startTime 開始時のUnix時間
	 * @param endTime   終了時のUnix時間
	 * @return 処理時間（ミリ秒小数点第三位の精度）
	 */
	public static double calcElapsedTimeByMilliSeconds(long startTime, long endTime) {
		return TimeUnit.NANOSECONDS.toMicros(endTime - startTime) / 1000d;
	}

}
