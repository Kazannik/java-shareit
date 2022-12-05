package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                         @Valid @NotNull @RequestBody ItemRequestRequestDto requestDto) {
        log.info("Create request {}, userId={}", requestDto, userId);
        return itemRequestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Get all requests by userId={}", userId);
        return itemRequestClient.getAllByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestParam(name = "from", required = false) Integer from,
                                         @RequestParam(name = "size", required = false) Integer size) {
        log.info("Get all requests, userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                          @NotNull @PathVariable long requestId) {
        log.info("Get request {}, userId={}", requestId, userId);
        return itemRequestClient.getById(userId, requestId);
    }
}
