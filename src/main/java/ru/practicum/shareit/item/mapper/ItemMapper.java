package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {

    Item toItem(Long userId, ItemDto dto);

    Item patchItem(Item item, ItemDto dto);

    ItemDto toItemDto(Item item);

}
