package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
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
        Item item = itemMapper.toItem(userService.findById(userId), itemDto);
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
        itemRepository.save(item);
        log.debug("Item updated. Before: {}, after: {}", previous, item);
        return item;
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
    public List<Item> findAllByOwnerId(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<ItemDto> findAllByOwnerIdToDto(Long userId) {
        return findAllByOwnerId(userId).stream()
                .map(i -> toDto(userId, i))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchTextIgnoreCase(text.toLowerCase());
        }
    }

    @Override
    public List<ItemDto> searchToDto(Long userId, String text) {
        return search(text).stream()
                .map(i -> toDto(userId, i))
                .collect(Collectors.toList());
    }

    private void itemValidated(Item item) {
        if (!userService.existsById(item.getOwner().getId())) {
            log.debug("User {} not found", item.getOwner().getId());
            throw new NotFoundException(String.format("User %s not found.", item.getOwner().getId()));
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
        commentRepository.save(comment);
        return comment;
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
