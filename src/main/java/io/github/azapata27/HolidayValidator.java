package io.github.azapata27;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface HolidayValidator {

    boolean isHoliday(LocalDate date);
    List<LocalDate> getHolidaysForYear(Year year);
}
