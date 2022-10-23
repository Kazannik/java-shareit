package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> findItemById(Long id);

    List<Item> findAll(Long userId);

    List<Item> search(String text);

    void clear();
}
