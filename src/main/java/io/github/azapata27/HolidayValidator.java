package io.github.azapata27;

import io.github.azapata27.model.Holiday;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface HolidayValidator<T extends Holiday> {

    /**
     * Checks if a given date is a holiday.
     *
     * @param date the {@link LocalDate} to check; must not be null
     * @return {@code true} if the date is a holiday, {@code false} otherwise
     * @throws IllegalArgumentException if {@code date} is null
     */
    boolean isHoliday(LocalDate date);

    /**
     * Retrieves all holiday dates for a specific year.
     *
     * @param year the {@link Year} to query; must not be null
     * @return a list of {@link LocalDate} representing all holidays in the given year
     * @throws IllegalArgumentException if {@code year} is null
     */
    List<LocalDate> getHolidayDatesForYear(Year year);

    /**
     * Retrieves all holiday objects for a specific year.
     *
     * @param year the {@link Year} to query; must not be null
     * @return a list of holiday objects of type {@code T} for the given year
     * @throws IllegalArgumentException if {@code year} is null
     */
    List<T> getHolidaysForYear(Year year);
    /**
     * Finds the next holiday after the specified date.
     *
     * @param date the date from which to search
     * @return the next holiday as a {@link LocalDate}, or {@code null} if no holidays remain in the year
     * @throws IllegalArgumentException if {@code date} is null
     */
    Optional<LocalDate> getNextHolidayDate(LocalDate date);
    /**
     * Finds the next holiday after the specified date.
     *
     * @param date the date from which to search
     * @return the next holiday as a {@link Holiday}, or {@code null} if no holidays remain in the year
     * @throws IllegalArgumentException if {@code date} is null
     */
    Optional<T> getNextHoliday(LocalDate date);
    /**
     * Finds the most recent holiday before the specified date.
     *
     * @param date the date from which to search
     * @return the previous holiday as a {@link LocalDate}
     * @throws IllegalArgumentException if {@code date} is null
     */
    Optional<LocalDate> getPreviousHolidayDate(LocalDate date);
    /**
     * Finds the most recent holiday before the specified date.
     *
     * @param date the date from which to search
     * @return the previous holiday as a {@link LocalDate}
     * @throws IllegalArgumentException if {@code date} is null
     */
    Optional<T> getPreviousHoliday(LocalDate date);

    /**
     * Determines if a given holiday is part of a long weekend (puente).
     *
     * @param date the date to check
     * @return true if the holiday is part of a long weekend, false otherwise
     * @throws IllegalArgumentException if {@code date} is null
     */
    boolean isLongWeekend(LocalDate date);
}
