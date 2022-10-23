package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(Item item);

    Item updateItem(Long userId, Item item);

    Item findItemById(Long id);

    List<Item> findAll(Long userId);

    List<Item> search(String text);

    void clear();
}
