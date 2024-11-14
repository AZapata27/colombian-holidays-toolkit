package io.github.azapata27;

import io.github.azapata27.enums.ColombianHolidayType;

import java.time.Month;

public class Holiday {
    private final String name;
    private final Month month;
    private final int day;
    private final ColombianHolidayType type;
    private final int easterOffset;

    private Holiday(Builder builder) {
        this.name = builder.name;
        this.month = builder.month;
        this.day = builder.day;
        this.type = builder.type;
        this.easterOffset = builder.easterOffset;
    }

    public String getName() {
        return name;
    }

    public Month getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public ColombianHolidayType getType() {
        return type;
    }

    public int getEasterOffset() {
        return easterOffset;
    }

    public boolean isTransferable() {
        return type.isTransferable();
    }


    public static class Builder {
        private String name;
        private Month month;
        private int day;
        private ColombianHolidayType type;
        private int easterOffset;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(Month month, int day) {
            this.month = month;
            this.day = day;
            return this;
        }

        public Builder type(ColombianHolidayType type) {
            this.type = type;
            return this;
        }

        public Builder easterOffset(int offset) {
            this.easterOffset = offset;
            return this;
        }

        public Holiday build() {
            return new Holiday(this);
        }
    }
}