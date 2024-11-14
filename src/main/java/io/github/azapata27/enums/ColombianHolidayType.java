package io.github.azapata27.enums;

public enum ColombianHolidayType {

    FIXED(HolidayType.FIXED, "Fixed Holiday"),
    EASTER_BASED(HolidayType.RELATIVE_TO_DATE, "Easter Related Holiday"),
    EASTER_BASED_TRANSFERABLE(HolidayType.TRANSFERABLE, "Transferable Easter Based Holiday"),
    TRANSFERABLE(HolidayType.TRANSFERABLE, "Transferable Holiday");

    private final HolidayType type;
    private final String description;

    ColombianHolidayType(HolidayType type, String description) {
        this.type = type;
        this.description = description;
    }

    public HolidayType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTransferable() {
        return getType() == HolidayType.TRANSFERABLE;
    }
}