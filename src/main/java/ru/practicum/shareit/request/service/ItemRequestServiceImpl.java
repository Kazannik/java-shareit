package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
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
import java.util.stream.Collectors;

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
        if (pageValidated(from, size)) {
            return itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId,
                    PageRequest.of(from, size)).toList();
        } else {
            return itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId);
        }
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
        if (pageValidated(from, size)) {
            return itemRequestRepository.findAllByRequestorNotLikeOrderByCreatedAsc(user,
                    PageRequest.of(from, size)).toList();
        } else {
            return itemRequestRepository.findAllByRequestorNotLikeOrderByCreatedAsc(user);
        }
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

    private boolean pageValidated(Integer from, Integer size) {
        if (from != null && size != null) {
            if (from < 0) {
                log.debug("Row index {} must not be less than zero", from);
                throw new ArgumentNotValidException(
                        String.format("Row index %s must not be less than zero.", from));
            } else if (size < 1) {
                log.debug("Size {} of the page to be returned, must be greater than zero", size);
                throw new ArgumentNotValidException(
                        String.format("Size %s of the page to be returned, must be greater than zero.", size));
            }
            return true;
        } else {
            return false;
        }
    }
}
