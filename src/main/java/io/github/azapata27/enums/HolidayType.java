package io.github.azapata27.enums;

/**
 * Base types for holiday calculations across different calendars and cultures.
 * This enum represents the fundamental ways holidays can be determined:
 *
 * <ul>
 *   <li>{@link #FIXED} - Holidays that occur on the same date every year in the Gregorian calendar
 *       (e.g., Christmas - December 25, New Year - January 1)</li>
 *
 *   <li>{@link #RELATIVE_TO_DATE} - Holidays that are calculated based on another date
 *       (e.g., Easter Sunday determines Good Friday [-2 days] and Ascension Day [+39 days])</li>
 *
 *   <li>{@link #TRANSFERABLE} - Holidays that move to another date based on specific rules
 *       (e.g., holidays that move to Monday when falling on weekend)</li>
 *
 *   <li>{@link #FORMULA_BASED} - Holidays determined by mathematical or astronomical calculations
 *       (e.g., Ramadan, which starts based on lunar visibility calculations)</li>
 *
 *   <li>{@link #LUNAR_BASED} - Holidays based on the lunar calendar
 *       (e.g., Chinese New Year, Islamic holidays)</li>
 * </ul>
 *
 * <p>This classification allows for the implementation of holiday calculations
 * across different religious and cultural traditions worldwide.</p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * // Example of categorizing holidays
 * HolidayType christmasType = HolidayType.FIXED;             // December 25
 * HolidayType goodFridayType = HolidayType.RELATIVE_TO_DATE; // Relative to Easter
 * HolidayType laborDayType = HolidayType.TRANSFERABLE;       // Moves to Monday
 * }</pre>
 *
 * @see ColombianHolidayType For a specific implementation using these base types
 */
public enum HolidayType {
    /**
     * Holidays that occur on the same date every year in the Gregorian calendar.
     * Example: Christmas (December 25), Independence Day (July 20)
     */
    FIXED,

    /**
     * Holidays that are calculated based on another reference date.
     * Example: Good Friday (2 days before Easter Sunday)
     */
    RELATIVE_TO_DATE,

    /**
     * Holidays that can be moved to another date based on specific rules.
     * Example: Holidays that move to Monday when falling on weekend
     */
    TRANSFERABLE,

    /**
     * Holidays determined by specific mathematical or astronomical calculations.
     * Example: Ramadan start date based on lunar visibility calculations
     */
    FORMULA_BASED,

    /**
     * Holidays based on lunar calendar systems.
     * Example: Chinese New Year, Islamic holidays
     */
    LUNAR_BASED;
}