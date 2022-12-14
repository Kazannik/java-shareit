package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @NotNull @RequestBody ItemDto itemDto,
                          @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @NotNull @RequestBody ItemDto itemDto,
                          @NotNull @PathVariable Long itemId,
                          @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@NotNull @PathVariable Long itemId,
                            @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.findByIdToDto(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAll(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                 @RequestParam(required = false) Integer from,
                                 @RequestParam(required = false) Integer size) {
        return itemService.findAllByOwnerIdToDto(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                @NotNull @RequestParam(name = "text") String text,
                                @RequestParam(required = false) Integer from,
                                @RequestParam(required = false) Integer size) {
        return itemService.searchToDto(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto create(@NotNull @PathVariable Long itemId,
                             @NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                             @Valid @NotNull @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
