package com.flab.gettoticket.util;

import org.springframework.cglib.core.Local;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class TimeUnitUtil {

    public static long getMillsecondsToSec(long milliseconds) {
        return TimeUnit.SECONDS.convert(milliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * playAt(+1일) 값을 사용하여 날짜 포맷을 지정 후 milliseconds로 반환
     * @param playAt 2024-12-01
     * @return
     */
    public static long getLocalDateToMillSec(LocalDate playAt) {
        LocalDate playDate = playAt.plusDays(1);
        LocalDateTime playDateTime = playDate.atTime(LocalTime.MAX);    //2024-12-02 23:59:59.999999

        return playDateTime
                .atZone(ZoneId.systemDefault()) // 시스템 기본 시간대를 사용
                .toInstant()
                .toEpochMilli();
    }

    /**
     * targetMilliSec 과 현재 시간의 시간 차를 초 단위로 반환
     * @param targetMilliSec
     * @return
     */
    public static long getCurrDateTimeDiff(long targetMilliSec) {
        long currentMilliSec = System.currentTimeMillis();

        // 시간 차이 초 단위로 반환
        return (targetMilliSec - currentMilliSec) / 1000;
    }

    /**
     *
     * @param epochTime System.currentTimeMillis()
     * @return
     */
    public static String getMillisecondsToDateTime(long epochTime) {
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime)    //milliseconds를 Instant로 변환, epoch(1970.1.1) 기준 시간에서 밀리초를 기반으로 Instant 생성
                                    .atZone(ZoneId.systemDefault()) //시스템의 기본 타임 존 적용
                                    .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        return dateTime.format(formatter);
    }

}
