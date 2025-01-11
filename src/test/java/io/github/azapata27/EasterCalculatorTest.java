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

    private static Stream<Arguments> provideEasterDates() {
        return Stream.of(
            Arguments.of(2023, Month.APRIL, 9),
            Arguments.of(2024, Month.MARCH, 31),
            Arguments.of(2025, Month.APRIL, 20),
            Arguments.of(2026, Month.APRIL, 5)
        );
    }
}