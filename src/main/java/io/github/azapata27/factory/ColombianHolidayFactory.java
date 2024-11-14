package io.github.azapata27.factory;

import io.github.azapata27.model.ColombianHoliday;
import io.github.azapata27.enums.ColombianHolidayType;

import java.time.Month;
import java.util.List;

/**
 * Factory for creating Colombian holidays according to Law 51 of 1983.
 */
public class ColombianHolidayFactory {

    private ColombianHolidayFactory() {}

    public static List<ColombianHoliday> createFixedHolidays() {
        return List.of(
                new ColombianHoliday.Builder()
                        .name("Año Nuevo")
                        .date(Month.JANUARY, 1)
                        .type(ColombianHolidayType.FIXED_CIVIL)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Día del Trabajo")
                        .date(Month.MAY, 1)
                        .type(ColombianHolidayType.FIXED_CIVIL)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Día de la Independencia")
                        .date(Month.JULY, 20)
                        .type(ColombianHolidayType.FIXED_CIVIL)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Batalla de Boyacá")
                        .date(Month.AUGUST, 7)
                        .type(ColombianHolidayType.FIXED_CIVIL)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Inmaculada Concepción")
                        .date(Month.DECEMBER, 8)
                        .type(ColombianHolidayType.FIXED_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Navidad")
                        .date(Month.DECEMBER, 25)
                        .type(ColombianHolidayType.FIXED_RELIGIOUS)
                        .build()
        );
    }

    public static List<ColombianHoliday> createEasterBasedHolidays() {
        return List.of(
                new ColombianHoliday.Builder()
                        .name("Jueves Santo")
                        .type(ColombianHolidayType.EASTER_BASED_RELIGIOUS)
                        .easterOffset(-3)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Viernes Santo")
                        .type(ColombianHolidayType.EASTER_BASED_RELIGIOUS)
                        .easterOffset(-2)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Ascensión del Señor")
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .easterOffset(39)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Corpus Christi")
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .easterOffset(60)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Sagrado Corazón")
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .easterOffset(68)
                        .build()
        );
    }

    public static List<ColombianHoliday> createTransferableHolidays() {
        return List.of(
                new ColombianHoliday.Builder()
                        .name("Día de los Reyes Magos")
                        .date(Month.JANUARY, 6)
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Día de San José")
                        .date(Month.MARCH, 19)
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("San Pedro y San Pablo")
                        .date(Month.JUNE, 29)
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Asunción de la Virgen")
                        .date(Month.AUGUST, 15)
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Día de la Diversidad Étnica y Cultural")
                        .date(Month.OCTOBER, 12)
                        .type(ColombianHolidayType.TRANSFERABLE_CIVIL)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Día de Todos los Santos")
                        .date(Month.NOVEMBER, 1)
                        .type(ColombianHolidayType.TRANSFERABLE_RELIGIOUS)
                        .build(),
                new ColombianHoliday.Builder()
                        .name("Independencia de Cartagena")
                        .date(Month.NOVEMBER, 11)
                        .type(ColombianHolidayType.TRANSFERABLE_CIVIL)
                        .build()
        );
    }
}