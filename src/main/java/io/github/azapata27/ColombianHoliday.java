package io.github.azapata27;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public class ColombianHoliday implements HolidayValidator {

    @Override
    public boolean isHoliday(LocalDate date) {
        return false;
    }

    @Override
    public List<LocalDate> getHolidaysForYear(Year year) {
        return List.of();
    }
}
