package io.github.azapata27.model;

import io.github.azapata27.enums.ColombianHolidayType;

public class ColombianHoliday extends Holiday {
    private final ColombianHolidayType colombianType;
    private final int easterOffset;

    private ColombianHoliday(Builder builder) {
        super(builder);
        this.colombianType = builder.colombianType;
        this.easterOffset = builder.easterOffset;
    }

    public ColombianHolidayType getColombianType() {
        return colombianType;
    }

    public int getEasterOffset() {
        return easterOffset;
    }

    public static class Builder extends Holiday.Builder<Builder> {
        private ColombianHolidayType colombianType;
        private int easterOffset;

        public Builder from(ColombianHoliday holiday) {
            super.name(holiday.getName());
            super.date(holiday.getMonth(), holiday.getDay());
            super.date(holiday.getDate());
            super.type(holiday.getType());
            this.colombianType = holiday.getColombianType();
            this.easterOffset = holiday.getEasterOffset();
            return this;
        }

        public Builder type(ColombianHolidayType type) {
            this.colombianType = type;
            super.type(type.getType());
            return this;
        }

        public Builder easterOffset(int offset) {
            this.easterOffset = offset;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColombianHoliday build() {
            return new ColombianHoliday(this);
        }
    }
}