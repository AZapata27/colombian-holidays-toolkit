package io.github.azapata27;

import io.github.azapata27.enums.ColombianHolidayType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ColombianHoliday implements HolidayValidator {

    private final List<Holiday> fixedHolidays;
    private final List<Holiday> easterBasedHolidays;
    private final List<Holiday> transferableHolidays;

    public ColombianHoliday() {
        this.fixedHolidays = initFixedHolidays();
        this.easterBasedHolidays = initEasterBasedHolidays();
        this.transferableHolidays = initTransferableHolidays();
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return getHolidaysForYear(Year.of(date.getYear())).contains(date);
    }

    @Override
    public List<LocalDate> getHolidaysForYear(Year year) {
        List<LocalDate> holidays = new ArrayList<>();

        for (Holiday holiday : fixedHolidays) {
            holidays.add(LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay()));
        }

        LocalDate easterSunday = EasterCalculator.calculateEasterSunday(year.getValue());
        for (Holiday holiday : easterBasedHolidays) {
            LocalDate easterBasedDate = easterSunday.plusDays(holiday.getEasterOffset());
            holidays.add(holiday.isTransferable() ? adjustToNextMonday(easterBasedDate) : easterBasedDate);
        }

        for (Holiday holiday : transferableHolidays) {
            LocalDate baseDate = LocalDate.of(year.getValue(), holiday.getMonth(), holiday.getDay());
            holidays.add(adjustToNextMonday(baseDate));
        }

        return holidays;
    }

    private LocalDate adjustToNextMonday(LocalDate date) {
        if (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return date;
    }

    private List<Holiday> initFixedHolidays() {
        return List.of(
                new Holiday.Builder()
                        .name("Año Nuevo")
                        .date(Month.JANUARY, 1)
                        .type(ColombianHolidayType.FIXED)
                        .build(),
                new Holiday.Builder()
                        .name("Día del Trabajo")
                        .date(Month.MAY, 1)
                        .type(ColombianHolidayType.FIXED)
                        .build(),
                new Holiday.Builder()
                        .name("Día de la Independencia")
                        .date(Month.JULY, 20)
                        .type(ColombianHolidayType.FIXED)
                        .build(),
                new Holiday.Builder()
                        .name("Batalla de Boyacá")
                        .date(Month.AUGUST, 7)
                        .type(ColombianHolidayType.FIXED)
                        .build(),
                new Holiday.Builder()
                        .name("Inmaculada Concepción")
                        .date(Month.DECEMBER, 8)
                        .type(ColombianHolidayType.FIXED)
                        .build(),
                new Holiday.Builder()
                        .name("Navidad")
                        .date(Month.DECEMBER, 25)
                        .type(ColombianHolidayType.FIXED)
                        .build()
        );
    }

    private List<Holiday> initEasterBasedHolidays() {
        return List.of(
                new Holiday.Builder()
                        .name("Jueves Santo")
                        .type(ColombianHolidayType.EASTER_BASED)
                        .easterOffset(-3)
                        .build(),
                new Holiday.Builder()
                        .name("Viernes Santo")
                        .type(ColombianHolidayType.EASTER_BASED)
                        .easterOffset(-2)
                        .build(),
                new Holiday.Builder()
                        .name("Ascensión del Señor")
                        .type(ColombianHolidayType.EASTER_BASED_TRANSFERABLE)
                        .easterOffset(39)
                        .build(),
                new Holiday.Builder()
                        .name("Corpus Christi")
                        .type(ColombianHolidayType.EASTER_BASED_TRANSFERABLE)
                        .easterOffset(60)
                        .build(),
                new Holiday.Builder()
                        .name("Sagrado Corazón")
                        .type(ColombianHolidayType.EASTER_BASED_TRANSFERABLE)
                        .easterOffset(68)
                        .build()
        );
    }

    private List<Holiday> initTransferableHolidays() {
        return List.of(
                new Holiday.Builder()
                        .name("Reyes Magos")
                        .date(Month.JANUARY, 6)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("San José")
                        .date(Month.MARCH, 19)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("San Pedro y San Pablo")
                        .date(Month.JUNE, 29)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("Asunción de la Virgen")
                        .date(Month.AUGUST, 15)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("Día de la Raza")
                        .date(Month.OCTOBER, 12)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("Todos los Santos")
                        .date(Month.NOVEMBER, 1)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build(),
                new Holiday.Builder()
                        .name("Independencia de Cartagena")
                        .date(Month.NOVEMBER, 11)
                        .type(ColombianHolidayType.TRANSFERABLE)
                        .build()
        );
    }
}
