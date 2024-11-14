package io.github.azapata27;

import io.github.azapata27.model.Holiday;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface HolidayValidator<T extends Holiday> {

    boolean isHoliday(LocalDate date);
    List<LocalDate> getHolidayDatesForYear(Year year);
    List<T> getHolidaysForYear(Year year);
}
