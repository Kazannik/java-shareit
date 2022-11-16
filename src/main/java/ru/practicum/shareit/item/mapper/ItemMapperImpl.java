package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toItem(User owner, ItemDto dto, ItemRequest request) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setRequest(request);
        item.setOwner(owner);
        return item;
    }

    @Override
    public Item patchItem(Item item, ItemDto dto) {
        Item patchedItem = new Item();
        patchedItem.setId(item.getId());
        patchedItem.setName(dto.getName() != null ? dto.getName() : item.getName());
        patchedItem.setDescription(dto.getDescription() != null ? dto.getDescription() : item.getDescription());
        patchedItem.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : item.isAvailable());
        patchedItem.setOwner(item.getOwner());
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
