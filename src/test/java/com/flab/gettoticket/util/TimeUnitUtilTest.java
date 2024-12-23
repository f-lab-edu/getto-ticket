package com.flab.gettoticket.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUnitUtilTest {

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
        long epochTime = 1734447853162L; //2024.12.18 00:04:13

        // When
        String result = TimeUnitUtil.getMillisecondsToDateTime(epochTime);

        // Then
        String expected = "2024.12.18 00:04:13"; //기대값
        assertThat(result).isEqualTo(expected);
    }

}