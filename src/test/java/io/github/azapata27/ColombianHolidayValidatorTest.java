package io.github.azapata27;

import io.github.azapata27.enums.ColombianHolidayType;
import io.github.azapata27.model.ColombianHoliday;
import io.github.azapata27.model.Holiday;
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
        List<Holiday> holidays = holidayValidator.getHolidaysForYear(Year.of(2024));

        assertEquals(18, holidays.size(), "Should return 18 holidays for 2024");

        // Verify a fixed holiday
        Holiday newYear = findHolidayByDate(holidays, LocalDate.of(2024, Month.JANUARY, 1));
        assertNotNull(newYear, "Should contain New Year");
        assertEquals("Año Nuevo", newYear.getName());
        assertEquals(ColombianHolidayType.FIXED_CIVIL, newYear.getType());

        // Verify an Easter-based holiday
        Holiday holyThursday = findHolidayByDate(holidays, LocalDate.of(2024, Month.MARCH, 28));
        assertNotNull(holyThursday, "Should contain Holy Thursday");
        assertEquals("Jueves Santo", holyThursday.getName());
        assertEquals(ColombianHolidayType.EASTER_BASED_RELIGIOUS, holyThursday.getType());

        // Verify a transferable holiday
        Holiday epiphany = findHolidayByDate(holidays, LocalDate.of(2024, Month.JANUARY, 8));
        assertNotNull(epiphany, "Should contain Epiphany");
        assertEquals("Día de Reyes (Epifanía)", epiphany.getName());
        assertEquals(ColombianHolidayType.TRANSFERABLE_RELIGIOUS, epiphany.getType());
        assertTrue(epiphany.isTransferable());
    }

    @ParameterizedTest
    @MethodSource("provideHolidayDates2024")
    void shouldContainAllHolidaysWithCorrectInfo(Month month, int day, String expectedName) {
        List<Holiday> holidays = holidayValidator.getHolidaysForYear(Year.of(2024));
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

    // Helper method to find a holiday by date
    private Holiday findHolidayByDate(List<Holiday> holidays, LocalDate date) {
        return holidays.stream()
                .filter(h -> h.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }
}