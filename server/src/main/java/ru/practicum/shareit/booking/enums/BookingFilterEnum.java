package ru.practicum.shareit.booking.enums;

public enum BookingFilterEnum {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED, Unknown;

    public static BookingFilterEnum of(String state) {
        for (BookingFilterEnum command : values()) {
            if (command.name().equalsIgnoreCase(state)) {
                return command;
            }
        }
        return BookingFilterEnum.Unknown;
    }
}
