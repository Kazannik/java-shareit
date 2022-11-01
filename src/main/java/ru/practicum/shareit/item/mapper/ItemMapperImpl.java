package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item toItem(Long userId, ItemDto dto) {
        Item item = new Item(dto.getName(), dto.getDescription(), dto.getAvailable(), userId);
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
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
