package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toItem(User owner, ItemDto dto) {
        Item item = new Item(dto.getName(), dto.getDescription(), dto.getAvailable(), owner);
        item.setId(dto.getId());
        return item;
    }

    @Override
    public Item patchItem(Item item, ItemDto dto) {
        Item patchedItem = new Item(dto.getName() != null ? dto.getName() : item.getName(),
                dto.getDescription() != null ? dto.getDescription() : item.getDescription(),
                dto.getAvailable() != null ? dto.getAvailable() : item.isAvailable(),
                item.getOwner());
        patchedItem.setId(item.getId());
        return patchedItem;
    }

    @Override
    public ItemDto toDto(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        if (item == null) {
            return null;
        }
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                lastBooking,
                nextBooking,
                comments);
    }

    @Override
    public ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                new ArrayList<>());
    }
}
