package ru.practicum.shareit.booking.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.booking.enums.BookingStatusEnum.WAITING;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                itemMapper.toDto(booking.getItem()),
                userMapper.toDto(booking.getBooker()),
                booking.getStatus());
    }

    @Override
    public Booking toBooking(@NonNull BookingDto dto, @NonNull Item item, @NonNull User user) {
        return new Booking(dto.getId() != null ? dto.getId() : 0L,
                dto.getStart(),
                dto.getEnd(),
                item,
                user,
                dto.getStatus() != null ? dto.getStatus() : WAITING);
    }
}
