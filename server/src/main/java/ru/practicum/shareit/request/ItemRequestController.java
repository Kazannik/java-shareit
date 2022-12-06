package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Valid @NotNull @RequestBody ItemRequestDto itemRequestDto,
                                 @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUser(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                             @RequestParam(required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        return itemRequestService.getAllByUserToDto(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                       @RequestParam(required = false) Integer from,
                                       @RequestParam(required = false) Integer size) {
        return itemRequestService.getAllToDto(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@NotNull @PathVariable Long requestId,
                                  @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.getByIdToDto(requestId, userId);
    }
}
