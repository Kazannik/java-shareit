package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    BookingDto create(Long userId, BookingDto bookingDto);

    Booking approve(Long bookingId, Long userId, Boolean approved);

    BookingDto approveToDto(Long bookingId, Long userId, Boolean approved);

    List<Booking> findAllByOwner(Long userId, String state, Integer from, Integer size);

    List<BookingDto> findAllByOwnerToDto(Long userId, String state, Integer from, Integer size);

    List<Booking> findAllByUser(Long userId, String state, Integer from, Integer size);

    List<BookingDto> findAllByUserToDto(Long userId, String state, Integer from, Integer size);

    Booking findByIdAndBookerId(Long bookingId, Long userId);

    BookingDto findByIdAndBookerIdToDto(Long bookingId, Long userId);

    Boolean isAccessForBooker(Long userId, Long itemId);

    Booking findLastBooking(Long itemId);

    BookingDto findLastBookingToDto(Long itemId);

    Booking findNextBooking(Long itemId);

    BookingDto findNextBookingToDto(Long itemId);

}
