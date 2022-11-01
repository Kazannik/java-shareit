package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @NotNull @RequestBody ItemDto itemDto,
                              @Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        Item item = itemMapper.toItem(userId, itemDto);
        item = itemService.createItem(item);
        return itemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @NotNull @RequestBody ItemDto itemDto, @PathVariable Long itemId,
                              @Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        Item oldItem = itemService.findItemById(itemId);
        Item item = itemService.updateItem(userId, itemMapper.patchItem(oldItem, itemDto));
        return itemMapper.toItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        return itemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> findAll(@Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.findAll(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String text) {
        return itemService.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
