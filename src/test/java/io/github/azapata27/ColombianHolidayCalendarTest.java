package io.github.azapata27;

import io.github.azapata27.calculator.EasterCalculator;
import io.github.azapata27.enums.ColombianHolidayType;
import io.github.azapata27.enums.HolidayType;
import io.github.azapata27.model.ColombianHoliday;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies {@link ColombianHolidayValidator} against the official Colombian holiday calendars
 * (Law 51 of 1983, "Ley Emiliani") for 2024, 2025 and 2026.
 */
class ColombianHolidayCalendarTest {

    private final ColombianHolidayValidator validator = new ColombianHolidayValidator();

    @Nested
    @DisplayName("Official calendars")
    class OfficialCalendars {

        @Test
        void holidayDates2024MatchOfficialCalendar() {
            assertThat(validator.getHolidayDatesForYear(Year.of(2024))).containsExactly(
                    of(2024, 1, 1), of(2024, 1, 8), of(2024, 3, 25), of(2024, 3, 28), of(2024, 3, 29),
                    of(2024, 5, 1), of(2024, 5, 13), of(2024, 6, 3), of(2024, 6, 10), of(2024, 7, 1),
                    of(2024, 7, 20), of(2024, 8, 7), of(2024, 8, 19), of(2024, 10, 14), of(2024, 11, 4),
                    of(2024, 11, 11), of(2024, 12, 8), of(2024, 12, 25));
        }

        @Test
        void holidayDates2025MatchOfficialCalendar() {
            // 18 holidays but only 17 distinct days: San Pedro y San Pablo and Sagrado Corazón share 2025-06-30
            assertThat(validator.getHolidayDatesForYear(Year.of(2025))).containsExactly(
                    of(2025, 1, 1), of(2025, 1, 6), of(2025, 3, 24), of(2025, 4, 17), of(2025, 4, 18),
                    of(2025, 5, 1), of(2025, 6, 2), of(2025, 6, 23), of(2025, 6, 30), of(2025, 7, 20),
                    of(2025, 8, 7), of(2025, 8, 18), of(2025, 10, 13), of(2025, 11, 3), of(2025, 11, 17),
                    of(2025, 12, 8), of(2025, 12, 25));
        }

        @Test
        void holidayDates2026MatchOfficialCalendar() {
            assertThat(validator.getHolidayDatesForYear(Year.of(2026))).containsExactly(
                    of(2026, 1, 1), of(2026, 1, 12), of(2026, 3, 23), of(2026, 4, 2), of(2026, 4, 3),
                    of(2026, 5, 1), of(2026, 5, 18), of(2026, 6, 8), of(2026, 6, 15), of(2026, 6, 29),
                    of(2026, 7, 20), of(2026, 8, 7), of(2026, 8, 17), of(2026, 10, 12), of(2026, 11, 2),
                    of(2026, 11, 16), of(2026, 12, 8), of(2026, 12, 25));
        }

        @ParameterizedTest(name = "{1} on {0}")
        @CsvSource({
                "2025-01-01, Año Nuevo",
                "2025-01-06, Día de los Reyes Magos",
                "2025-03-24, Día de San José",
                "2025-04-17, Jueves Santo",
                "2025-04-18, Viernes Santo",
                "2025-05-01, Día del Trabajo",
                "2025-06-02, Ascensión del Señor",
                "2025-06-23, Corpus Christi",
                "2025-07-20, Día de la Independencia",
                "2025-08-07, Batalla de Boyacá",
                "2025-08-18, Asunción de la Virgen",
                "2025-10-13, Día de la Diversidad Étnica y Cultural",
                "2025-11-03, Día de Todos los Santos",
                "2025-11-17, Independencia de Cartagena",
                "2025-12-08, Inmaculada Concepción",
                "2025-12-25, Navidad"
        })
        void holidayObjects2025HaveOfficialNames(LocalDate date, String name) {
            assertThat(validator.getHolidaysForYear(Year.of(2025)))
                    .filteredOn(h -> h.getDate().equals(date))
                    .extracting(ColombianHoliday::getName)
                    .containsExactly(name);
        }

        @Test
        void holidayObjectsKeepBothHolidaysThatShareADay() {
            List<ColombianHoliday> holidays = validator.getHolidaysForYear(Year.of(2025));

            assertThat(holidays).hasSize(18);
            assertThat(holidays)
                    .filteredOn(h -> h.getDate().equals(of(2025, 6, 30)))
                    .extracting(ColombianHoliday::getName)
                    .containsExactlyInAnyOrder("Sagrado Corazón", "San Pedro y San Pablo");
        }

        @Test
        void holidayObjectsAreSortedByDate() {
            assertThat(validator.getHolidaysForYear(Year.of(2025)))
                    .extracting(ColombianHoliday::getDate)
                    .isSorted();
        }
    }

    @Nested
    @DisplayName("Fixed holidays")
    class FixedHolidays {

        @ParameterizedTest
        @ValueSource(ints = {1990, 2023, 2024, 2025, 2026, 2100})
        void fixedHolidaysNeverMove(int year) {
            assertThat(validator.getHolidayDatesForYear(Year.of(year))).contains(
                    of(year, 1, 1), of(year, 5, 1), of(year, 7, 20),
                    of(year, 8, 7), of(year, 12, 8), of(year, 12, 25));
        }

        @Test
        void fixedHolidayOnWeekendIsNotMovedToMonday() {
            // 2025-07-20 and 2024-12-08 are Sundays: fixed holidays stay on their date
            assertThat(of(2025, 7, 20).getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
            assertThat(validator.isHoliday(of(2025, 7, 20))).isTrue();
            assertThat(validator.isHoliday(of(2025, 7, 21))).isFalse();
            assertThat(validator.isHoliday(of(2024, 12, 8))).isTrue();
            assertThat(validator.isHoliday(of(2024, 12, 9))).isFalse();
        }

        @Test
        void fixedHolidaysCarryTheirType() {
            List<ColombianHoliday> holidays = validator.getHolidaysForYear(Year.of(2025));

            assertThat(holidays)
                    .filteredOn(h -> h.getColombianType() == ColombianHolidayType.FIXED_CIVIL)
                    .extracting(ColombianHoliday::getName)
                    .containsExactly("Año Nuevo", "Día del Trabajo", "Día de la Independencia", "Batalla de Boyacá");
            assertThat(holidays)
                    .filteredOn(h -> h.getColombianType() == ColombianHolidayType.FIXED_RELIGIOUS)
                    .extracting(ColombianHoliday::getName)
                    .containsExactly("Inmaculada Concepción", "Navidad");
            assertThat(holidays)
                    .filteredOn(h -> h.getType() == HolidayType.FIXED)
                    .allMatch(h -> !h.isTransferable());
        }
    }

    @Nested
    @DisplayName("Easter-based holidays")
    class EasterBasedHolidays {

        @ParameterizedTest(name = "Holy week {0}")
        @CsvSource({
                "2024, 2024-03-28, 2024-03-29",
                "2025, 2025-04-17, 2025-04-18",
                "2026, 2026-04-02, 2026-04-03"
        })
        void holyThursdayAndGoodFridayAreNotMoved(int year, LocalDate holyThursday, LocalDate goodFriday) {
            assertThat(validator.getHolidaysForYear(Year.of(year)))
                    .filteredOn(h -> h.getColombianType() == ColombianHolidayType.EASTER_BASED_RELIGIOUS)
                    .extracting(ColombianHoliday::getDate)
                    .containsExactly(holyThursday, goodFriday);
            assertThat(holyThursday.getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
            assertThat(goodFriday.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        }

        @Test
        void easterSundayItselfIsNotAHoliday() {
            assertThat(validator.isHoliday(EasterCalculator.calculateEasterSunday(2025))).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = {2000, 2008, 2011, 2024, 2025, 2038, 2050})
        void movableFeastsAreTheMondaysAfterEasterPlus39_60_68(int year) {
            LocalDate easter = EasterCalculator.calculateEasterSunday(year);
            // Ascension (Thu +39), Corpus Christi (Thu +60), Sacred Heart (Fri +68) -> following Monday
            assertThat(validator.getHolidayDatesForYear(Year.of(year)))
                    .contains(easter.plusDays(43), easter.plusDays(64), easter.plusDays(71));
        }
    }

    @Nested
    @DisplayName("Ley Emiliani (moved to Monday)")
    class LeyEmiliani {

        @Test
        void holidayAlreadyOnMondayStays() {
            // 2025-01-06 (Reyes Magos) and 2026-10-12 (Diversidad) are Mondays
            assertThat(validator.isHoliday(of(2025, 1, 6))).isTrue();
            assertThat(validator.isHoliday(of(2025, 1, 13))).isFalse();
            assertThat(validator.isHoliday(of(2026, 10, 12))).isTrue();
        }

        @Test
        void holidayOnSundayMovesToNextDay() {
            // 2025-10-12 is a Sunday -> Monday 2025-10-13
            assertThat(validator.isHoliday(of(2025, 10, 12))).isFalse();
            assertThat(validator.isHoliday(of(2025, 10, 13))).isTrue();
        }

        @Test
        void holidayOnTuesdayMovesSixDaysLater() {
            // 2025-11-11 is a Tuesday -> Monday 2025-11-17
            assertThat(validator.isHoliday(of(2025, 11, 11))).isFalse();
            assertThat(validator.isHoliday(of(2025, 11, 17))).isTrue();
        }

        @Test
        void transferableHolidaysAlwaysLandOnMondayWithinTheFollowingWeek() {
            Map<String, ColombianHolidayBase> bases = Map.of(
                    "Día de los Reyes Magos", new ColombianHolidayBase(Month.JANUARY, 6),
                    "Día de San José", new ColombianHolidayBase(Month.MARCH, 19),
                    "San Pedro y San Pablo", new ColombianHolidayBase(Month.JUNE, 29),
                    "Asunción de la Virgen", new ColombianHolidayBase(Month.AUGUST, 15),
                    "Día de la Diversidad Étnica y Cultural", new ColombianHolidayBase(Month.OCTOBER, 12),
                    "Día de Todos los Santos", new ColombianHolidayBase(Month.NOVEMBER, 1),
                    "Independencia de Cartagena", new ColombianHolidayBase(Month.NOVEMBER, 11));

            IntStream.rangeClosed(2000, 2100).forEach(year ->
                    validator.getHolidaysForYear(Year.of(year)).stream()
                            .filter(ColombianHoliday::isTransferable)
                            .forEach(holiday -> {
                                assertThat(holiday.getDate().getDayOfWeek())
                                        .as("%s %d", holiday.getName(), year)
                                        .isEqualTo(DayOfWeek.MONDAY);
                                ColombianHolidayBase base = bases.get(holiday.getName());
                                if (base != null) {
                                    long shift = ChronoUnit.DAYS.between(of(year, base.month, base.day), holiday.getDate());
                                    assertThat(shift).as("%s %d", holiday.getName(), year).isBetween(0L, 6L);
                                }
                            }));
        }

        @Test
        void transferableTypesAreFlaggedAsTransferable() {
            assertThat(validator.getHolidaysForYear(Year.of(2025)))
                    .filteredOn(ColombianHoliday::isTransferable)
                    .hasSize(10)
                    .allMatch(h -> h.getColombianType() == ColombianHolidayType.TRANSFERABLE_CIVIL
                            || h.getColombianType() == ColombianHolidayType.TRANSFERABLE_RELIGIOUS);
        }

        private record ColombianHolidayBase(Month month, int day) {
        }
    }

    @Nested
    @DisplayName("Counts per month and year")
    class Counts {

        @ParameterizedTest
        @ValueSource(ints = {1984, 2000, 2023, 2024, 2025, 2026, 2027, 2100})
        void everyYearHasEighteenHolidays(int year) {
            assertThat(validator.getHolidaysForYear(Year.of(year))).hasSize(18);
        }

        @Test
        void holidaysPerMonth2024() {
            assertThat(countByMonth(validator.getHolidayDatesForYear(Year.of(2024)))).containsExactlyInAnyOrderEntriesOf(Map.of(
                    Month.JANUARY, 2L, Month.MARCH, 3L, Month.MAY, 2L, Month.JUNE, 2L, Month.JULY, 2L,
                    Month.AUGUST, 2L, Month.OCTOBER, 1L, Month.NOVEMBER, 2L, Month.DECEMBER, 2L));
        }

        @Test
        void holidaysPerMonth2025CountsSharedDayOnceAsDateButTwiceAsHoliday() {
            List<ColombianHoliday> holidays = validator.getHolidaysForYear(Year.of(2025));
            List<LocalDate> dates = validator.getHolidayDatesForYear(Year.of(2025));

            assertThat(holidays).filteredOn(h -> h.getDate().getMonth() == Month.JUNE).hasSize(4);
            assertThat(dates).filteredOn(d -> d.getMonth() == Month.JUNE).hasSize(3);
            assertThat(dates).hasSize(17).doesNotHaveDuplicates();
            assertThat(countByMonth(dates)).doesNotContainKeys(Month.FEBRUARY, Month.SEPTEMBER);
        }

        private Map<Month, Long> countByMonth(List<LocalDate> dates) {
            return dates.stream().collect(Collectors.groupingBy(LocalDate::getMonth, Collectors.counting()));
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @ParameterizedTest
        @ValueSource(ints = {1984, 2000, 2024, 2038, 2099, 2100})
        void allHolidaysBelongToTheRequestedYear(int year) {
            assertThat(validator.getHolidayDatesForYear(Year.of(year)))
                    .allMatch(d -> d.getYear() == year)
                    .isSorted()
                    .doesNotHaveDuplicates();
        }

        @Test
        void leapDayIsNotAHolidayAndNextHolidayIsFound() {
            assertThat(validator.isHoliday(of(2024, 2, 29))).isFalse();
            assertThat(validator.getNextHolidayDate(of(2024, 2, 29))).contains(of(2024, 3, 25));
            assertThat(validator.getPreviousHolidayDate(of(2024, 2, 29))).contains(of(2024, 1, 8));
        }

        @Test
        void leapAndCommonYearsHaveTheSameNumberOfHolidays() {
            assertThat(validator.getHolidaysForYear(Year.of(2028))).hasSameSizeAs(validator.getHolidaysForYear(Year.of(2027)));
        }

        @Test
        void nextHolidayIsSearchedOnlyWithinTheSameYear() {
            assertThat(validator.getNextHolidayDate(of(2025, 12, 24))).contains(of(2025, 12, 25));
            assertThat(validator.getNextHolidayDate(of(2025, 12, 25))).isEmpty();
            assertThat(validator.getNextHoliday(of(2025, 12, 26))).isEmpty();
        }

        @Test
        void previousHolidayIsSearchedOnlyWithinTheSameYear() {
            assertThat(validator.getPreviousHolidayDate(of(2025, 1, 2))).contains(of(2025, 1, 1));
            assertThat(validator.getPreviousHolidayDate(of(2025, 1, 1))).isEmpty();
            assertThat(validator.getPreviousHoliday(of(2025, 1, 1))).isEmpty();
        }

        @Test
        void nextHolidayIsStrictlyAfterTheGivenDate() {
            assertThat(validator.getNextHoliday(of(2025, 6, 23)))
                    .map(ColombianHoliday::getDate)
                    .contains(of(2025, 6, 30));
        }

        @Test
        void previousHolidayIsTheClosestOneBefore() {
            assertThat(validator.getPreviousHoliday(of(2025, 7, 1)))
                    .map(ColombianHoliday::getDate)
                    .contains(of(2025, 6, 30));
        }

        @Test
        void longWeekendDetectsHolidayMonday() {
            assertThat(validator.isLongWeekend(of(2025, 10, 10))).isTrue();  // Fri -> Mon 13 Oct
            assertThat(validator.isLongWeekend(of(2025, 10, 17))).isFalse(); // Fri -> Mon 20 Oct
            assertThat(validator.isLongWeekend(of(2026, 1, 9))).isTrue();    // Fri -> Mon 12 Jan
        }

        @Test
        void longWeekendWorksAcrossYearBoundary() {
            // 2024-12-31 (Tue) -> next Monday is 2025-01-06, Reyes Magos
            assertThat(validator.isLongWeekend(of(2024, 12, 31))).isTrue();
            // 2025-12-31 (Wed) -> next Monday is 2026-01-05, a working day (Reyes Magos moves to 2026-01-12)
            assertThat(validator.isLongWeekend(of(2025, 12, 31))).isFalse();
        }

        @Test
        void nullArgumentsAreRejected() {
            assertThatThrownBy(() -> validator.isHoliday(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> validator.getHolidayDatesForYear(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> validator.getHolidaysForYear(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> validator.getNextHoliday(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> validator.getPreviousHoliday(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> validator.isLongWeekend(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void returnedListsAreIndependentCopies() {
            List<LocalDate> first = validator.getHolidayDatesForYear(Year.of(2025));
            first.clear();

            assertThat(validator.getHolidayDatesForYear(Year.of(2025))).hasSize(17);
        }
    }
}
