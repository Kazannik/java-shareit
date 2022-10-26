package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository()
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items;

    private long key = 1;

    private long nextIdGenerator() {
        return key++;
    }

    private void generatorReset() {
        key = 1;
    }

    @Override
    public Item createItem(Item item) {
        item = new Item(item.getName(), item.getDescription(), item.isAvailable(), item.getOwner());
        item.setId(nextIdGenerator());
        items.put(item.getId(), item);
        log.debug("{} has been added.", item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.debug("Item {} updated.", item);
        return item;
    }

    @Override
    public Optional<Item> findItemById(Long id) {
        Item item = items.get(id);
        if (item != null) {
            log.debug("Search result: {}", item);
            return Optional.of(item);
        } else {
            log.debug("Item {} not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values().stream()
                    .filter(Item::isAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void clear() {
        items.clear();
        generatorReset();
    }
}
