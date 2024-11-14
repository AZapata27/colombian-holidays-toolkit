package io.github.azapata27;

import io.github.azapata27.calculator.EasterCalculator;
import io.github.azapata27.enums.ColombianHolidayType;
import io.github.azapata27.factory.ColombianHolidayFactory;
import io.github.azapata27.model.ColombianHoliday;
import io.github.azapata27.model.Holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Colombian holidays according to Law 51 of 1983 and Colombian official calendar.
 * Handles five distinct types of holidays:
 * <ul>
 *   <li>{@link ColombianHolidayType#FIXED_CIVIL} - Civil holidays with fixed dates
 *       (e.g., Año Nuevo, Día del Trabajo, Independencia Nacional, Batalla de Boyacá)</li>
 *
 *   <li>{@link ColombianHolidayType#FIXED_RELIGIOUS} - Religious holidays with fixed dates
 *       (e.g., Navidad, Inmaculada Concepción)</li>
 *
 *   <li>{@link ColombianHolidayType#EASTER_BASED_RELIGIOUS} - Religious holidays calculated from Easter Sunday
 *       that remain on their actual date (e.g., Jueves Santo, Viernes Santo)</li>
 *
 *   <li>{@link ColombianHolidayType#TRANSFERABLE_RELIGIOUS} - Religious holidays that always move to following Monday
 *       (e.g., Ascensión del Señor, Corpus Christi, Sagrado Corazón)</li>
 *
 *   <li>{@link ColombianHolidayType#TRANSFERABLE_CIVIL} - Civil holidays that always move to following Monday
 *       (e.g., Día de la Diversidad Étnica y Cultural, Independencia de Cartagena)</li>
 * </ul>
 *
 * <p><b>Legal References:</b></p>
 * <ul>
 *   <li><a href="https://www.funcionpublica.gov.co/eva/gestornormativo/norma.php?i=4954">Law 51 of 1983</a> -
 *       Regulates the transfer of holidays to Mondays</li>
 *   <li><a href="https://es.wikipedia.org/wiki/Anexo:D%C3%ADas_festivos_en_Colombia">Official Colombian Holidays</a> -
 *       Complete list of Colombian holidays</li>
 * </ul>
 *
 * <p><b>Usage examples:</b></p>
 * <pre>{@code
 * HolidayValidator validator = new ColombianHolidayValidator();
 *
 * // Check if a specific date is a holiday
 * boolean isHoliday = validator.isHoliday(LocalDate.now());
 *
 * // Get all holidays for year 2024
 * List<LocalDate> holidays2024 = validator.getHolidaysForYear(Year.of(2024));
 * }</pre>
 *
 * @see HolidayValidator
 * @see ColombianHolidayType
 * @see ColombianHolidayFactory
 * @see EasterCalculator
 */
public class ColombianHolidayValidator implements HolidayValidator<ColombianHoliday> {

    private final List<ColombianHoliday> fixedHolidays;
    private final List<ColombianHoliday> easterBasedHolidays;
    private final List<ColombianHoliday> transferableHolidays;


    /**
     * Constructs a new ColombianHoliday instance.
     * Initializes all holiday lists using {@link ColombianHolidayFactory} to create:
     * <ul>
     *   <li>Fixed holidays (both civil and religious)</li>
     *   <li>Easter-based holidays (including transferable ones)</li>
     *   <li>Transferable holidays (civil ones that move to Monday)</li>
     * </ul>
     */
    public ColombianHolidayValidator() {
        this.fixedHolidays = ColombianHolidayFactory.createFixedHolidays();
        this.easterBasedHolidays = ColombianHolidayFactory.createEasterBasedHolidays();
        this.transferableHolidays = ColombianHolidayFactory.createTransferableHolidays();
    }

    /**
     * Determines if a given date is a Colombian holiday.
     * Checks against all types of holidays (fixed, easter-based, and transferable).
     *
     * @param date the date to check
     * @return true if the date is a holiday, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return getHolidayDatesForYear(Year.of(date.getYear())).contains(date);
    }

    /**
     * Gets all Colombian holidays for a specific year as LocalDate objects.
     * Processes each type of holiday according to its rules:
     * <ul>
     *   <li>Fixed dates remain unchanged (e.g., January 1st, December 25th)</li>
     *   <li>Easter-based dates are calculated relative to Easter Sunday (e.g., Holy Thursday -3 days)</li>
     *   <li>Transferable dates are moved to the following Monday</li>
     * </ul>
     *
     * @param year the year to get holidays for
     * @return a list of LocalDate objects representing all holidays in the specified year
     * @see EasterCalculator#calculateEasterSunday(int)
     */
    @Override
    public List<LocalDate> getHolidayDatesForYear(Year year) {
        List<LocalDate> holidays = new ArrayList<>();

        for (ColombianHoliday holiday : fixedHolidays) {
            holidays.add(LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay()));
        }

        LocalDate easterSunday = EasterCalculator.calculateEasterSunday(year.getValue());
        for (ColombianHoliday holiday : easterBasedHolidays) {
            LocalDate easterBasedDate = easterSunday.plusDays(holiday.getEasterOffset());
            holidays.add(holiday.isTransferable() ? adjustToNextMonday(easterBasedDate) : easterBasedDate);
        }

        for (ColombianHoliday holiday : transferableHolidays) {
            LocalDate baseDate = LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay());
            holidays.add(adjustToNextMonday(baseDate));
        }

        return holidays;
    }


    /**
     * Gets all Colombian holidays for a specific year with complete holiday information.
     * Returns a list of Holiday objects containing:
     * <ul>
     *   <li>Fixed holidays: New Year, Labor Day, Christmas, etc.</li>
     *   <li>Easter-based holidays: Holy Thursday, Good Friday</li>
     *   <li>Easter-based transferable: Ascension Day, Corpus Christi</li>
     *   <li>Regular transferable: Epiphany, Saint Joseph's Day</li>
     * </ul>
     *
     * <p>Each Holiday object includes:</p>
     * <ul>
     *   <li>Holiday name in Spanish</li>
     *   <li>Type of holiday (FIXED, EASTER_BASED, etc.)</li>
     *   <li>Final calculated holiday date for the specified year</li>
     * </ul>
     *
     * @param year the year to get holidays for
     * @return a list of Holiday objects with complete holiday information for the specified year
     * @see Holiday
     * @see ColombianHoliday
     * @see EasterCalculator#calculateEasterSunday(int)
     */
    @Override
    public List<ColombianHoliday> getHolidaysForYear(Year year) {
        List<ColombianHoliday> holidays = new ArrayList<>();

        for (ColombianHoliday holiday : fixedHolidays) {
            LocalDate date = LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay());
            holidays.add(new ColombianHoliday.Builder()
                    .from(holiday)
                    .date(date)
                    .build());
        }

        LocalDate easterSunday = EasterCalculator.calculateEasterSunday(year.getValue());
        for (ColombianHoliday holiday : easterBasedHolidays) {
            LocalDate baseDate = easterSunday.plusDays(holiday.getEasterOffset());
            LocalDate finalDate = holiday.isTransferable() ?
                    adjustToNextMonday(baseDate) : baseDate;

            holidays.add(new ColombianHoliday.Builder()
                    .from(holiday)
                    .date(finalDate)
                    .build());
        }

        for (ColombianHoliday holiday : transferableHolidays) {
            LocalDate baseDate = LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay());
            LocalDate finalDate = adjustToNextMonday(baseDate);

            holidays.add(new ColombianHoliday.Builder()
                    .from(holiday)
                    .date(finalDate)
                    .build());
        }

        return holidays;
    }

    /**
     * Adjusts a date to the next Monday if it's not already a Monday.
     * Used for transferable holidays.
     *
     * @param date the date to potentially adjust
     * @return the same date if it's a Monday, otherwise the date of the next Monday
     */
    private LocalDate adjustToNextMonday(LocalDate date) {
        if (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return date;
    }
}
