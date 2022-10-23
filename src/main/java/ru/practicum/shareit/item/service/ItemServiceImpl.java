package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserService service;

    @Override
    public Item createItem(Item item) {
        itemValidated(item);
        Item createdItem = itemStorage.createItem(item);
        log.debug("{} has been added.", createdItem);
        return createdItem;
    }

    @Override
    public Item updateItem(Long userId, Item item) {
        itemValidated(item);
        Item previous = findItemById(item.getId());
        if (!userId.equals(previous.getOwner())) {
            log.debug("User {} access to the item {} is forbidden", userId, item.getId());
            throw new AccessForbiddenException(
                    String.format("User %s access to the item %s is forbidden.", userId, item.getId()));
        }
        itemStorage.updateItem(item);
        log.debug("Item updated. Before: {}, after: {}", previous, item);
        return item;
    }

    @Override
    public Item findItemById(Long id) {
        return itemStorage.findItemById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item %s not found.", id)));
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemStorage.findAll(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemStorage.search(text);
    }

    private void itemValidated(Item item){
        if (!service.existsUserById(item.getOwner())) {
            log.debug("User {} not found", item.getOwner());
            throw new NotFoundException(String.format("User %s not found.", item.getOwner()));
        }
        if (item.getName().isBlank()) {
            log.debug("Item {} name not correct", item.getOwner());
            throw new NullPointerException(String.format("Item %s name not correct.", item.getOwner()));
        }
        if (item.getDescription().isBlank()) {
            log.debug("Item {} description not correct", item.getOwner());
            throw new NullPointerException(String.format("Item %s description not correct.", item.getOwner()));
        }
    }

    @Override
    public void clear() {
        itemStorage.clear();
    }
}
