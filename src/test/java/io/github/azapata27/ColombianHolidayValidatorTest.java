package io.github.azapata27;

import io.github.azapata27.enums.ColombianHolidayType;
import io.github.azapata27.model.ColombianHoliday;
import io.github.azapata27.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColombianHolidayValidatorTest {

    private final HolidayValidator<ColombianHoliday> holidayValidator = new ColombianHolidayValidator();

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
    void shouldThrowExceptionWhenDaysIsHolidays() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.isHoliday(null), "The date must not be null");
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

    @Test
    void shouldThrowExceptionWhenGetAllHolidayDatesForYear() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getHolidayDatesForYear(null), "The date must not be null");
    }

    private static Stream<Arguments> provideHolidayDates2024() {
        return Stream.of(
                // Fixed Civil Holidays
                Arguments.of(Month.JANUARY, 1, "Año Nuevo"),
                Arguments.of(Month.MAY, 1, "Día del Trabajo"),
                Arguments.of(Month.JULY, 20, "Día de la Independencia"),
                Arguments.of(Month.AUGUST, 7, "Batalla de Boyacá"),

                // Fixed Religious Holidays
                Arguments.of(Month.DECEMBER, 8, "Inmaculada Concepción"),
                Arguments.of(Month.DECEMBER, 25, "Navidad"),

                // Easter Based Religious Holidays
                Arguments.of(Month.MARCH, 28, "Jueves Santo"),
                Arguments.of(Month.MARCH, 29, "Viernes Santo"),
                Arguments.of(Month.MAY, 13, "Ascensión del Señor"),
                Arguments.of(Month.JUNE, 3, "Corpus Christi"),
                Arguments.of(Month.JUNE, 10, "Sagrado Corazón"),

                // Transferable Religious Holidays
                Arguments.of(Month.JANUARY, 8, "Día de los Reyes Magos"),
                Arguments.of(Month.MARCH, 25, "Día de San José"),
                Arguments.of(Month.JULY, 1, "San Pedro y San Pablo"),
                Arguments.of(Month.AUGUST, 19, "Asunción de la Virgen"),
                Arguments.of(Month.NOVEMBER, 4, "Día de Todos los Santos"),

                // Transferable Civil Holidays
                Arguments.of(Month.OCTOBER, 14, "Día de la Diversidad Étnica y Cultural"),
                Arguments.of(Month.NOVEMBER, 11, "Independencia de Cartagena")
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

    @Test
    void shouldReturnCorrectHolidayObjects() {
        List<ColombianHoliday> holidays = holidayValidator.getHolidaysForYear(Year.of(2024));

        assertEquals(18, holidays.size(), "Should return 18 holidays for 2024");

        // Verify a fixed holiday
        ColombianHoliday newYear = findHolidayByDate(holidays, LocalDate.of(2024, Month.JANUARY, 1));
        assertNotNull(newYear, "Should contain New Year");
        assertEquals("Año Nuevo", newYear.getName());
        assertEquals(ColombianHolidayType.FIXED_CIVIL, newYear.getColombianType());

        // Verify an Easter-based holiday
        ColombianHoliday holyThursday = findHolidayByDate(holidays, LocalDate.of(2024, Month.MARCH, 28));
        assertNotNull(holyThursday, "Should contain Holy Thursday");
        assertEquals("Jueves Santo", holyThursday.getName());
        assertEquals(ColombianHolidayType.EASTER_BASED_RELIGIOUS, holyThursday.getColombianType());

        // Verify a transferable holiday
        ColombianHoliday epiphany = findHolidayByDate(holidays, LocalDate.of(2024, Month.JANUARY, 8));
        assertNotNull(epiphany, "Should contain Epiphany");
        assertEquals("Día de los Reyes Magos", epiphany.getName());
        assertEquals(ColombianHolidayType.TRANSFERABLE_RELIGIOUS, epiphany.getColombianType());
        assertTrue(epiphany.isTransferable());
    }

    @ParameterizedTest
    @MethodSource("provideHolidayDates2024")
    void shouldContainAllHolidaysWithCorrectInfo(Month month, int day, String expectedName) {
        List<ColombianHoliday> holidays = holidayValidator.getHolidaysForYear(Year.of(2024));
        Holiday holiday = findHolidayByDate(holidays, LocalDate.of(2024, month, day));

        assertNotNull(holiday,
                String.format("Should contain holiday on %s %d", month, day));
        assertEquals(expectedName, holiday.getName(),
                String.format("Holiday on %s %d should be %s", month, day, expectedName));
    }

    @Test
    void shouldHandleLeapYearsWithHolidayObjects() {
        List<ColombianHoliday> holidays2024 = holidayValidator.getHolidaysForYear(Year.of(2024));
        List<ColombianHoliday> holidays2025 = holidayValidator.getHolidaysForYear(Year.of(2025));

        ColombianHoliday easterBased2024 = holidays2024.stream()
                .filter(h -> h.getColombianType() == ColombianHolidayType.EASTER_BASED_RELIGIOUS)
                .findFirst()
                .orElseThrow();

        ColombianHoliday easterBased2025 = holidays2025.stream()
                .filter(h -> h.getColombianType() == ColombianHolidayType.EASTER_BASED_RELIGIOUS)
                .findFirst()
                .orElseThrow();

        assertNotEquals(easterBased2024.getDate(), easterBased2025.getDate(),
                "Easter-based holidays should have different dates in consecutive years");
    }

    @Test
    void shouldPreserveHolidayInformation() {
        List<ColombianHoliday> holidays = holidayValidator.getHolidaysForYear(Year.of(2024));

        holidays.forEach(holiday -> {
            assertNotNull(holiday.getName(), "Holiday name should not be null");
            assertNotNull(holiday.getType(), "Holiday type should not be null");
            assertNotNull(holiday.getDate(), "Holiday date should not be null");

            if (holiday.getColombianType() == ColombianHolidayType.TRANSFERABLE_RELIGIOUS
                    || holiday.getColombianType() == ColombianHolidayType.TRANSFERABLE_CIVIL) {
                assertTrue(holiday.isTransferable(),
                        String.format("%s should be marked as transferable", holiday.getName()));
            }
        });
    }

    @Test
    void shouldThrowExceptionWhenGetAllHolidaysForYear() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getHolidaysForYear(null), "The date must not be null");
    }

    @Test
    void shouldReturnNextHolidayDateWhenGivenADate() {
        LocalDate today = LocalDate.of(2024, Month.JANUARY, 5); // A regular Friday
        Optional<LocalDate> nextHoliday = holidayValidator.getNextHolidayDate(today);

        assertTrue(nextHoliday.isPresent(), "There should be a next holiday after January 5th");
        assertEquals(LocalDate.of(2024, Month.JANUARY, 8), nextHoliday.get(), "Next holiday should be January 8th (Epiphany transferred)");

        today = LocalDate.of(2024, Month.MARCH, 29); // Good Friday
        nextHoliday = holidayValidator.getNextHolidayDate(today);

        assertTrue(nextHoliday.isPresent(), "There should be a next holiday after March 29th");
        assertEquals(LocalDate.of(2024, Month.MAY, 1), nextHoliday.get(), "Next holiday should be May 1st (Labor Day)");
    }

    @Test
    void shouldReturnEmptyWhenNoNextHolidayExists() {
        LocalDate lastDayOf2024 = LocalDate.of(2024, Month.DECEMBER, 31);
        Optional<LocalDate> nextHoliday = holidayValidator.getNextHolidayDate(lastDayOf2024);

        assertFalse(nextHoliday.isPresent(), "There should be no next holiday after December 31st of 2024");
    }

    @Test
    void shouldThrowExceptionWhenGetNextHolidayDateCalledWithNull() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getNextHolidayDate(null), "The date must not be null");
    }

    @Test
    void shouldReturnNextHolidayWhenGivenADate() {
        LocalDate today = LocalDate.of(2024, Month.JANUARY, 5); // A regular Friday
        Optional<ColombianHoliday> nextHoliday = holidayValidator.getNextHoliday(today);

        assertTrue(nextHoliday.isPresent(), "There should be a next holiday after January 5th");
        assertEquals("Día de los Reyes Magos", nextHoliday.get().getName(), "Next holiday should be Epiphany (January 8th)");

        today = LocalDate.of(2024, Month.MARCH, 29); // Good Friday
        nextHoliday = holidayValidator.getNextHoliday(today);

        assertTrue(nextHoliday.isPresent(), "There should be a next holiday after March 29th");
        assertEquals("Día del Trabajo", nextHoliday.get().getName(), "Next holiday should be May 1st (Labor Day)");
    }

    @Test
    void shouldReturnEmptyWhenNoNextHolidayExistsForHoliday() {
        LocalDate lastDayOf2024 = LocalDate.of(2024, Month.DECEMBER, 31);
        Optional<ColombianHoliday> nextHoliday = holidayValidator.getNextHoliday(lastDayOf2024);

        assertFalse(nextHoliday.isPresent(), "There should be no next holiday after December 31st of 2024");
    }

    @Test
    void shouldThrowExceptionWhenGetNextHolidayCalledWithNull() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getNextHoliday(null), "The date must not be null");
    }

    @Test
    void shouldReturnPreviousHolidayDateWhenGivenADate() {
        LocalDate today = LocalDate.of(2024, Month.JANUARY, 5); // A regular Friday
        Optional<LocalDate> previousHoliday = holidayValidator.getPreviousHolidayDate(today);

        assertTrue(previousHoliday.isPresent(), "There should be a previous holiday before January 5th");
        assertEquals(LocalDate.of(2024, Month.JANUARY, 1), previousHoliday.get(), "Previous holiday should be January 1st (New Year's Day)");

        today = LocalDate.of(2024, Month.MAY, 3); // A Friday after Labor Day
        previousHoliday = holidayValidator.getPreviousHolidayDate(today);

        assertTrue(previousHoliday.isPresent(), "There should be a previous holiday before May 3rd");
        assertEquals(LocalDate.of(2024, Month.MAY, 1), previousHoliday.get(), "Previous holiday should be May 1st (Labor Day)");
    }

    @Test
    void shouldReturnEmptyWhenNoPreviousHolidayExists() {
        LocalDate firstDayOf2024 = LocalDate.of(2024, Month.JANUARY, 1);
        Optional<LocalDate> previousHoliday = holidayValidator.getPreviousHolidayDate(firstDayOf2024);

        assertFalse(previousHoliday.isPresent(), "There should be no previous holiday before January 1st of 2024");
    }

    @Test
    void shouldThrowExceptionWhenGetPreviousHolidayDateCalledWithNull() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getPreviousHolidayDate(null), "The date must not be null");
    }

    @Test
    void shouldReturnPreviousHolidayWhenGivenADate() {
        LocalDate today = LocalDate.of(2024, Month.JANUARY, 5); // A regular Friday
        Optional<ColombianHoliday> previousHoliday = holidayValidator.getPreviousHoliday(today);

        assertTrue(previousHoliday.isPresent(), "There should be a previous holiday before January 5th");
        assertEquals("Año Nuevo", previousHoliday.get().getName(), "Previous holiday should be January 1st (New Year's Day)");

        today = LocalDate.of(2024, Month.MAY, 3); // A Friday after Labor Day
        previousHoliday = holidayValidator.getPreviousHoliday(today);

        assertTrue(previousHoliday.isPresent(), "There should be a previous holiday before May 3rd");
        assertEquals("Día del Trabajo", previousHoliday.get().getName(), "Previous holiday should be May 1st (Labor Day)");
    }

    @Test
    void shouldReturnEmptyWhenNoPreviousHolidayExistsForHoliday() {
        LocalDate firstDayOf2024 = LocalDate.of(2024, Month.JANUARY, 1);
        Optional<ColombianHoliday> previousHoliday = holidayValidator.getPreviousHoliday(firstDayOf2024);

        assertFalse(previousHoliday.isPresent(), "There should be no previous holiday before January 1st of 2024");
    }

    @Test
    void shouldThrowExceptionWhenGetPreviousHolidayCalledWithNull() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.getPreviousHoliday(null), "The date must not be null");
    }

    @Test
    void shouldThrowExceptionWhenIsLongWeekendCalledWithNull() {
        assertThrows(IllegalArgumentException.class, () -> holidayValidator.isLongWeekend(null), "The date must not be null");
    }

    @Test
    void testIsLongWeekendWhenMondayIsHoliday() {

        LocalDate weekendDate = LocalDate.of(2024, 10, 10);
        
        assertTrue(holidayValidator.isLongWeekend(weekendDate), "The next Monday is a holiday, so it should be a long weekend.");
    }


    // Helper method to find a holiday by date
    private ColombianHoliday findHolidayByDate(List<ColombianHoliday> holidays, LocalDate date) {
        return holidays.stream()
                .filter(h -> h.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }
}