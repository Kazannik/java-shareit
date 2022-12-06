package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                         @Valid @NotNull @RequestBody ItemRequestDto requestDto) {
        log.info("Create item {}, userId={}", requestDto, userId);
        return itemClient.createItem(userId, requestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                         @NotNull @PathVariable long itemId,
                                         @Valid @NotNull @RequestBody ItemRequestDto requestDto) {
        log.info("Update item {}, userId={}", requestDto, userId);
        return itemClient.updateItem(userId, itemId, requestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                           @NotNull @PathVariable long itemId) {
        log.info("Get item by Id {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                          @RequestParam(name = "from", required = false) Integer from,
                                          @RequestParam(name = "size", required = false) Integer size) {
        log.info("Get all items, userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                         @NotNull @RequestParam(name = "text") String text,
                                         @RequestParam(name = "from", required = false) Integer from,
                                         @RequestParam(name = "size", required = false) Integer size) {
        log.info("Search items, userId={}, from={}, size={}", userId, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> create(@NotNull @RequestHeader(HEADER_USER_ID) long userId,
                                         @NotNull @PathVariable long itemId,
                                         @Valid @NotNull @RequestBody CommentRequestDto requestDto) {
        log.info("Create comment {}, itemId={}, userId={}", requestDto, itemId, userId);
        return itemClient.createComment(userId, itemId, requestDto);
    }
}
