package io.github.azapata27.enums;

public enum HolidayType {
    FIXED,              // Holidays that always fall on the same date
    RELATIVE_TO_DATE,   // Holidays that depend on another date (e.g., Easter)
    TRANSFERABLE,       // Holidays that move to the next business day
    FORMULA_BASED,      // Holidays calculated by formula (e.g., Ramadan)
    LUNAR_BASED;        // Holidays based on lunar calendar
}