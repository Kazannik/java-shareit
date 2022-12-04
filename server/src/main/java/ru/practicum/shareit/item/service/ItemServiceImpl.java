package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.AccessForbiddenException;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.PaginationValid.pageValidated;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public Item create(Item item) {
        itemValidated(item);
        Item createdItem = itemRepository.save(item);
        log.debug("{} has been added.", createdItem);
        return createdItem;
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(String.format("Request %s not found.",
                            itemDto.getRequestId())));
        }
        Item item = itemMapper.toItem(userService.findById(userId), itemDto, itemRequest);
        return toDto(userId, create(item));
    }

    @Transactional
    @Override
    public Item update(Long userId, Item item) {
        itemValidated(item);
        Item previous = findById(item.getId());
        if (!userId.equals(previous.getOwner().getId())) {
            log.debug("User {} access to the item {} is forbidden", userId, item.getId());
            throw new AccessForbiddenException(
                    String.format("User %s access to the item %s is forbidden.", userId, item.getId()));
        }
        Item updatedItem = itemRepository.save(item);
        log.debug("Item updated. Before: {}, after: {}", previous, updatedItem);
        return updatedItem;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item oldItem = findById(itemId);
        Item item = update(userId, itemMapper.patchItem(oldItem, itemDto));
        return toDto(userId, item);
    }

    @Transactional(readOnly = true)
    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item %s not found.", itemId)));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto findByIdToDto(Long userId, Long itemId) {
        return toDto(userId, findById(itemId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> findAllByOwnerId(Long userId, Integer from, Integer size) {
        Optional<PageRequest> pageRequest = pageValidated(from, size);
        return pageRequest
                .map(request -> itemRepository.findAllByOwnerId(userId, request).toList())
                .orElseGet(() -> itemRepository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDto> findAllByOwnerIdToDto(Long userId, Integer from, Integer size) {
        return findAllByOwnerId(userId, from, size).stream()
                .sorted(Comparator.comparing(Item::getId))
                .map(i -> toDto(userId, i))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAllByRequestId(Long requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }

    @Override
    public List<ItemDto> findAllByRequestIdToDto(Long userId, Long requestId) {
        return findAllByRequestId(requestId).stream()
                .map(i -> toDto(userId, i))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> search(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        } else {
            Optional<PageRequest> pageRequest = pageValidated(from, size);
            return pageRequest.map(request -> itemRepository.searchTextIgnoreCase(text,
                    request).toList()).orElseGet(() -> itemRepository.searchTextIgnoreCase(text));
        }
    }

    @Override
    public List<ItemDto> searchToDto(Long userId, String text, Integer from, Integer size) {
        return search(text, from, size).stream()
                .map(i -> toDto(userId, i))
                .collect(Collectors.toList());
    }

    private void itemValidated(Item item) {
        userService.userValidated(item.getOwner().getId());
        if (item.getName().isBlank()) {
            log.debug("Item {} name not correct", item.getOwner());
            throw new NullPointerException(String.format("Item %s name not correct.", item.getOwner()));
        } else if (item.getDescription().isBlank()) {
            log.debug("Item {} description not correct", item.getOwner());
            throw new NullPointerException(String.format("Item %s description not correct.", item.getOwner()));
        }
    }

    @Transactional
    @Override
    public Comment createComment(Long itemId, Long userId, Comment comment) {
        Item item = findById(itemId);
        User user = userService.findById(userId);
        if (!bookingService.isAccessForBooker(userId, itemId)) {
            log.debug("User {} does not have access to the Item {}", userId, itemId);
            throw new ArgumentNotValidException(
                    String.format("User %s does not have access to the Item %s.", userId, itemId));
        }
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment createdComment = commentRepository.save(comment);
        log.debug("{} has been added.", createdComment);
        return createdComment;
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);
        return commentMapper.toDto(createComment(itemId, userId, comment));
    }

    private ItemDto toDto(Long userId, Item item) {
        BookingDto lastBooking = userId.equals(item.getOwner().getId()) ?
                bookingService.findLastBookingToDto(item.getId()) : null;
        BookingDto nextBooking = userId.equals(item.getOwner().getId()) ?
                bookingService.findNextBookingToDto(item.getId()) : null;
        return itemMapper.toDto(item, lastBooking, nextBooking, findCommentByItemIdToDto(item.getId()));
    }

    private List<CommentDto> findCommentByItemIdToDto(Long itemId) {
        return commentRepository.findAllByItemId(itemId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
