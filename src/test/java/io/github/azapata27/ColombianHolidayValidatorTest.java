package io.github.azapata27;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColombianHolidayValidatorTest {
    private final HolidayValidator holidayValidator = new ColombianHolidayValidator();

    @Test
    void shouldIdentifyFixedHolidays() {
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.JANUARY, 1)), "New Year should be holiday");
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.MAY, 1)), "Labor Day should be holiday");
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.DECEMBER, 25)), "Christmas should be holiday");
    }

    @Test
    void shouldIdentifyEasterBasedHolidays2024() {
        // 2024 Easter is March 31
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.MARCH, 28)), "Holy Thursday should be holiday");
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.MARCH, 29)), "Good Friday should be holiday");
    }

    @Test
    void shouldIdentifyTransferableHolidays2024() {
        // Epiphany (Jan 6, 2024 is Saturday, should be moved to Monday Jan 8)
        assertFalse(holidayValidator.isHoliday(LocalDate.of(2024, Month.JANUARY, 6)), "Original date should not be holiday");
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, Month.JANUARY, 8)), "Transferred date should be holiday");
    }

    @Test
    void shouldNotIdentifyRegularDaysAsHolidays() {
        assertFalse(holidayValidator.isHoliday(LocalDate.of(2024, Month.FEBRUARY, 13)), "Regular Tuesday should not be holiday");
        assertFalse(holidayValidator.isHoliday(LocalDate.of(2024, Month.MARCH, 20)), "Regular Wednesday should not be holiday");
        assertFalse(holidayValidator.isHoliday(LocalDate.of(2024, Month.APRIL, 10)), "Regular Wednesday should not be holiday");
    }

    @Test
    void shouldGetAllHolidaysForYear() {
        List<LocalDate> holidays2024 = holidayValidator.getHolidayDatesForYear(Year.of(2024));
        
        // 2024 should have 18 holidays
        assertEquals(18, holidays2024.size(), "Colombia should have 18 holidays in 2024");
        
        // Check some specific holidays
        assertTrue(holidays2024.contains(LocalDate.of(2024, Month.JANUARY, 1)), "Should contain New Year");
        assertTrue(holidays2024.contains(LocalDate.of(2024, Month.MARCH, 28)), "Should contain Holy Thursday");
        assertTrue(holidays2024.contains(LocalDate.of(2024, Month.MARCH, 29)), "Should contain Good Friday");
    }

    @ParameterizedTest
    @MethodSource("provideHolidayDates2024")
    void shouldIdentifyAllHolidays2024(Month month, int day, String description) {
        assertTrue(holidayValidator.isHoliday(LocalDate.of(2024, month, day)), 
                  String.format("%s should be a holiday", description));
    }

    private static Stream<Arguments> provideHolidayDates2024() {
        return Stream.of(
            Arguments.of(Month.JANUARY, 1, "New Year's Day"),
            Arguments.of(Month.JANUARY, 8, "Epiphany (moved from Jan 6)"),
            Arguments.of(Month.MARCH, 25, "St. Joseph's Day (moved from Mar 19)"),
            Arguments.of(Month.MARCH, 28, "Holy Thursday"),
            Arguments.of(Month.MARCH, 29, "Good Friday"),
            Arguments.of(Month.MAY, 1, "Labor Day"),
            Arguments.of(Month.MAY, 13, "Ascension Day"),
            Arguments.of(Month.JUNE, 3, "Corpus Christi"),
            Arguments.of(Month.JUNE, 10, "Sacred Heart"),
            Arguments.of(Month.JULY, 1, "St. Peter and St. Paul (moved from Jun 29)"),
            Arguments.of(Month.JULY, 20, "Independence Day"),
            Arguments.of(Month.AUGUST, 7, "Battle of Boyacá"),
            Arguments.of(Month.AUGUST, 19, "Assumption Day (moved from Aug 15)"),
            Arguments.of(Month.OCTOBER, 14, "Race Day (moved from Oct 12)"),
            Arguments.of(Month.NOVEMBER, 4, "All Saints' Day (moved from Nov 1)"),
            Arguments.of(Month.NOVEMBER, 11, "Independence of Cartagena"),
            Arguments.of(Month.DECEMBER, 8, "Immaculate Conception"),
            Arguments.of(Month.DECEMBER, 25, "Christmas Day")
        );
    }
    
    @Test
    void shouldHandleLeapYears() {
        List<LocalDate> holidays2024 = holidayValidator.getHolidayDatesForYear(Year.of(2024));
        List<LocalDate> holidays2025 = holidayValidator.getHolidayDatesForYear(Year.of(2025));
        
        assertNotEquals(
            holidays2024.stream()
                       .filter(d -> d.getMonth() == Month.MARCH)
                       .findFirst()
                       .orElseThrow(),
            holidays2025.stream()
                       .filter(d -> d.getMonth() == Month.MARCH)
                       .findFirst()
                       .orElseThrow(),
            "Easter-based holidays should be different in consecutive years"
        );
    }
}