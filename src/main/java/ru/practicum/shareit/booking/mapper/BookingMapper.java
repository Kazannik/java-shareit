package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper
public interface BookingMapper {

    Booking toBooking(BookingDto dto);

}
