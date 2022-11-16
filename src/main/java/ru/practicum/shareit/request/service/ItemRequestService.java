package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(Long userId, ItemRequest itemRequest);

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequest> getAllByUser(Long userId, Integer from, Integer  size);

    List<ItemRequestDto> getAllByUserToDto(Long userId, Integer  from, Integer  size);

    List<ItemRequest> getAll(Long userId, Integer  from, Integer  size);

    List<ItemRequestDto> getAllToDto(Long userId, Integer from, Integer size);

    ItemRequest getById(Long requestId, Long userId);

    ItemRequestDto getByIdToDto(Long requestId, Long userId);
}
