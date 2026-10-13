package io.github.azapata27;

import io.github.azapata27.calculator.EasterCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EasterCalculatorTest {
    
    @Test
    void shouldCalculateEasterFor2024() {
        LocalDate easter2024 = EasterCalculator.calculateEasterSunday(2024);
        assertEquals(LocalDate.of(2024, Month.MARCH, 31), easter2024);
    }

    @ParameterizedTest
    @MethodSource("provideEasterDates")
    void shouldCalculateEasterForMultipleYears(int year, Month expectedMonth, int expectedDay) {
        LocalDate calculatedEaster = EasterCalculator.calculateEasterSunday(year);
        LocalDate expectedEaster = LocalDate.of(year, expectedMonth, expectedDay);
        
        assertEquals(expectedEaster, calculatedEaster);
    }

    @Test
    void easterIsAlwaysASundayBetweenMarch22AndApril25() {
        for (int year = 1583; year <= 2500; year++) {
            LocalDate easter = EasterCalculator.calculateEasterSunday(year);
            assertEquals(java.time.DayOfWeek.SUNDAY, easter.getDayOfWeek(), "Easter " + year);
            assertFalse(easter.isBefore(LocalDate.of(year, Month.MARCH, 22)), "Easter " + year);
            assertFalse(easter.isAfter(LocalDate.of(year, Month.APRIL, 25)), "Easter " + year);
        }
    }

    private static Stream<Arguments> provideEasterDates() {
        return Stream.of(
            // Earliest (March 22) and latest (April 25) possible Easter dates
            Arguments.of(1818, Month.MARCH, 22),
            Arguments.of(1943, Month.APRIL, 25),
            Arguments.of(2038, Month.APRIL, 25),
            Arguments.of(2285, Month.MARCH, 22),
            // Century and leap-year boundaries
            Arguments.of(1900, Month.APRIL, 15),
            Arguments.of(2000, Month.APRIL, 23),
            Arguments.of(2100, Month.MARCH, 28),
            Arguments.of(2008, Month.MARCH, 23),
            Arguments.of(2011, Month.APRIL, 24),
            Arguments.of(2019, Month.APRIL, 21),
            Arguments.of(2023, Month.APRIL, 9),
            Arguments.of(2024, Month.MARCH, 31),
            Arguments.of(2025, Month.APRIL, 20),
            Arguments.of(2026, Month.APRIL, 5),
            Arguments.of(2027, Month.MARCH, 28),
            Arguments.of(2028, Month.APRIL, 16)
        );
    }
}