package ru.practicum.shareit.booking.mapper;

import lombok.NonNull;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface BookingMapper {

    BookingDto toDto(Booking booking);

    Booking toBooking(@NonNull BookingDto dto, @NonNull Item item, @NonNull User user);

}
