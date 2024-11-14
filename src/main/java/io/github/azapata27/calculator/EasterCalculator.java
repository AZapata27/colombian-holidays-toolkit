package io.github.azapata27.calculator;

import java.time.LocalDate;

/**
 * Calculator for determining Easter Sunday date using the Meeus/Jones/Butcher algorithm.
 * This algorithm is valid for the Gregorian calendar and works for dates past 1583.
 *
 * @see <a href="https://es.frwiki.wiki/wiki/Calcul_de_la_date_de_P%C3%A2ques_selon_la_m%C3%A9thode_de_Meeus">Meeus/Jones/Butcher Algorithm</a>
 */
public class EasterCalculator {

    private EasterCalculator() {
    }

    public static LocalDate calculateEasterSunday(int year) {
        // Calculate Golden Number and century
        int goldenNumber = year % 19;
        int century = year / 100;
        int yearOfCentury = year % 100;

        // Gregorian calendar corrections
        int leapYearCorrection = century / 4;
        int leapYearRemainder = century % 4;

        // Calculate moon's age (Epact)
        int lunarCorrection = (century + 8) / 25;
        int lunarAdjustment = (century - lunarCorrection + 1) / 3;
        int epact = (19 * goldenNumber + century - leapYearCorrection - lunarAdjustment + 15) % 30;

        // Calculate Easter day
        int daysToSunday = (32 + 2 * leapYearRemainder + 2 * (yearOfCentury / 4) - epact - (yearOfCentury % 4)) % 7;
        int paschalDays = epact + daysToSunday - 7 * ((goldenNumber + 11 * epact + 22 * daysToSunday) / 451) + 114;

        int month = paschalDays / 31;
        int day = (paschalDays % 31) + 1;

        return LocalDate.of(year, month, day);
    }
}