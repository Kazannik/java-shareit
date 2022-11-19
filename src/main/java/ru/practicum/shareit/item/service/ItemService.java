package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);

    ItemDto create(Long userId, ItemDto itemDto);

    Item update(Long userId, Item item);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    Item findById(Long itemId);

    ItemDto findByIdToDto(Long userId, Long itemId);

    List<Item> findAllByOwnerId(Long userId, Integer from, Integer size);

    List<ItemDto> findAllByOwnerIdToDto(Long userId, Integer from, Integer size);

    List<Item> findAllByRequestId(Long requestId);

    List<ItemDto> findAllByRequestIdToDto(Long userId, Long requestId);

    List<Item> search(String text, Integer from, Integer size);

    List<ItemDto> searchToDto(Long userId, String text, Integer from, Integer size);

    Comment createComment(Long itemId, Long userId, Comment comment);

    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);

}
