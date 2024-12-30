package com.flab.gettoticket.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUnitUtilTest {

    @BeforeAll
    static void setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void testGetMillsecondsToSec() {
        // Given
        long milliseconds = 10000;

        // When
        long result = TimeUnitUtil.getMillsecondsToSec(milliseconds);

        // Then
        assertThat(result).isEqualTo(10); //10000밀리초 = 10초
    }
    @Test
    void testMillisecondsToDateTime() {
        // Given
        long epochTime = 1734447853162L; //2024.12.17 15:04:13

        // When
        String result = TimeUnitUtil.getMillisecondsToDateTime(epochTime);

        // Then
        String expected = "2024.12.17 15:04:13"; //기대값
        assertThat(result).isEqualTo(expected);
    }

}