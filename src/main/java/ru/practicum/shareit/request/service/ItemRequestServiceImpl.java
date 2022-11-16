package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.PaginationValid.pageValidated;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional()
    @Override
    public ItemRequest create(Long userId, ItemRequest itemRequest) {
        User user = userService.findById(userId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        log.debug("{} has been added.", createdItemRequest);
        return createdItemRequest;
    }

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = create(userId, itemRequestMapper.toItemRequest(itemRequestDto));
        return itemRequestMapper.toDto(itemRequest, itemService.findAllByRequestIdToDto(userId, itemRequest.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequest> getAllByUser(Long userId, Integer from, Integer size) {
        userService.userValidated(userId);
        Optional<PageRequest> pageRequest = pageValidated(from, size);
        return pageRequest.map(request -> itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId,
                request).toList()).orElseGet(() -> itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId));
    }

    @Override
    public List<ItemRequestDto> getAllByUserToDto(Long userId, Integer from, Integer size) {
        return getAllByUser(userId, from, size).stream()
                .map(r -> itemRequestMapper.toDto(r, itemService.findAllByRequestIdToDto(userId, r.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequest> getAll(Long userId, Integer from, Integer size) {
        User user = userService.findById(userId);
        Optional<PageRequest> pageRequest = pageValidated(from, size);
        return pageRequest.map(request -> itemRequestRepository.findAllByRequestorNotLikeOrderByCreatedAsc(user,
                request).toList()).orElseGet(() -> itemRequestRepository.findAllByRequestorNotLikeOrderByCreatedAsc(user));
    }

    @Override
    public List<ItemRequestDto> getAllToDto(Long userId, Integer from, Integer size) {
        return getAll(userId, from, size).stream()
                .map(r -> itemRequestMapper.toDto(r, itemService.findAllByRequestIdToDto(userId, r.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequest getById(Long requestId, Long userId) {
        userService.userValidated(userId);
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request %s not found.", requestId)));
    }

    @Override
    public ItemRequestDto getByIdToDto(Long requestId, Long userId) {
        return itemRequestMapper.toDto(getById(requestId, userId),
                itemService.findAllByRequestIdToDto(userId, requestId));
    }
}
