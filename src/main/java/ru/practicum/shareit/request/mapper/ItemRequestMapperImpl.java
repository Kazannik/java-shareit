package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {
    @Override
    public ItemRequestDto toDto(ItemRequest itemRequest, List<ItemDto> items) {
        if (itemRequest == null) {
            return null;
        }
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items);
    }

    @Override
    public ItemRequest toItemRequest(ItemRequestDto dto) {
        return new ItemRequest(
                dto.getId(),
                dto.getDescription(),
                dto.getCreated());
    }
}
