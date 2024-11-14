package io.github.azapata27;

import java.time.LocalDate;

/**
 <a href="https://es.frwiki.wiki/wiki/Calcul_de_la_date_de_P%C3%A2ques_selon_la_m%C3%A9thode_de_Meeus">Meeus/Butcher Algoritm</a>
 */
public class EasterCalculator {

    private EasterCalculator() {}


    public static LocalDate calculateEasterSunday(int year) {
        // Meeus/Butcher Algoritm
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        
        return LocalDate.of(year, month, day);
    }
}
