package io.github.azapata27.model;

import io.github.azapata27.enums.HolidayType;
import java.time.LocalDate;
import java.time.Month;

public abstract class Holiday {
    private final String name;
    private final Month month;
    private final int day;
    private final HolidayType type;
    private final LocalDate date;

    protected Holiday(Builder<?> builder) {
        this.name = builder.name;
        this.month = builder.month;
        this.day = builder.day;
        this.type = builder.type;
        this.date = builder.date;
    }

    public String getName() { return name; }
    public Month getMonth() { return month; }
    public int getDay() { return day; }
    public HolidayType getType() { return type; }
    public LocalDate getDate() { return date; }

    public boolean isTransferable() {
        return type == HolidayType.TRANSFERABLE;
    }

    public abstract static class Builder<T extends Builder<T>> {
        private String name;
        private Month month;
        private int day;
        private HolidayType type;
        private LocalDate date;

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T date(Month month, int day) {
            this.month = month;
            this.day = day;
            return self();
        }

        public T type(HolidayType type) {
            this.type = type;
            return self();
        }

        public T date(LocalDate date) {
            this.date = date;
            return self();
        }

        protected abstract T self();
        public abstract Holiday build();
    }
}