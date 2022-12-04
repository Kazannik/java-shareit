package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemMapper {

    Item toItem(User owner, ItemDto dto, ItemRequest request);

    Item patchItem(Item item, ItemDto dto);

    ItemDto toDto(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments);

    ItemDto toDto(Item item);

}
