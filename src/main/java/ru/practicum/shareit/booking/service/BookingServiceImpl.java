package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingFilterEnum;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.queries.BookingQueryManager;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.enums.BookingStatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final String UNKNOWN_STATE_MESSAGE = "Unknown state: ";
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final BookingQueryManager bookingQueryManager;
    private final BookingMapper bookingMapper;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Transactional
    @Override
    public Booking create(Booking booking) {
        if (booking.getItem().getOwner().getId().equals(booking.getBooker().getId())) {
            log.debug("User {} does not have access to the Item {}",
                    booking.getItem().getOwner().getId(), booking.getItem().getId());
            throw new NotFoundException(String.format("User %s does not have access to the Item %s.",
                    booking.getItem().getOwner().getId(), booking.getItem().getId()));
        }
        if (!booking.getItem().isAvailable()) {
            log.debug("Item {} is not available", booking.getItem().getId());
            throw new ArgumentNotValidException(String.format("Item %s is not available.", booking.getItem().getId()));
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            log.debug("It is not possible to create a Booking. Start date not correct");
            throw new ArgumentNotValidException("It is not possible to create a Booking. Start date not correct.");
        }
        Booking createdBooking = bookingRepository.save(booking);
        log.debug("{} has been added.", createdBooking);
        return createdBooking;
    }

    @Override
    public BookingDto create(Long userId, BookingDto bookingDto) {
        User user = userService.findById(userId);
        Item item = findItemById(bookingDto.getItemId());
        Booking booking = bookingMapper.toBooking(bookingDto, item, user);
        booking = create(booking);
        return bookingMapper.toDto(booking);
    }

    @Transactional
    @Override
    public Booking approve(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ArgumentNotValidException(String.format("Booking %s not found.", bookingId)));
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            log.debug("User {} does not have a Booking {}", userId, bookingId);
            throw new NotFoundException(String.format("User %s does not have a Booking %s.", userId, bookingId));
        }
        if (!booking.getStatus().equals(WAITING)) {
            log.debug("Booking {} cannot be approved", bookingId);
            throw new ArgumentNotValidException(String.format("Booking %s cannot be approved.", bookingId));
        }
        if (approved) {
            booking.setStatus(APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }
        Booking approveBooking = bookingRepository.save(booking);
        log.debug("{} has been status.", approveBooking);
        return approveBooking;
    }

    @Override
    public BookingDto approveToDto(Long bookingId, Long userId, Boolean approved) {
        return bookingMapper.toDto(approve(bookingId, userId, approved));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> findAllByOwner(Long userId, String state, Integer from, Integer size) {
        return findAllBy(true, userId, state, from, size);
    }

    @Override
    public List<BookingDto> findAllByOwnerToDto(Long userId, String state,
                                                Integer from, Integer size) {
        return findAllByOwner(userId, state, from, size).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> findAllByUser(Long userId, String state, Integer from, Integer size) {
        return findAllBy(false, userId, state, from, size);
    }

    @Override
    public List<BookingDto> findAllByUserToDto(Long userId, String state,
                                               Integer from, Integer size) {
        return findAllByUser(userId, state, from, size).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Booking findByIdAndBookerId(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking %s not found.", bookingId)));
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            log.debug("No access to Booking {}.", bookingId);
            throw new NotFoundException(String.format("No access to Booking %s", bookingId));
        }
        return booking;
    }

    @Override
    public BookingDto findByIdAndBookerIdToDto(Long bookingId, Long userId) {
        return bookingMapper.toDto(findByIdAndBookerId(bookingId, userId));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean isAccessForBooker(Long userId, Long itemId) {
        return bookingRepository.existsAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId, itemId, APPROVED,
                LocalDateTime.now());
    }

    @Override
    public Booking findLastBooking(Long itemId) {
        return bookingRepository.findAllByItemIdOrderByStartAsc(itemId).stream().findFirst().orElse(null);
    }

    @Override
    public BookingDto findLastBookingToDto(Long itemId) {
        return bookingMapper.toDto(findLastBooking(itemId));
    }

    @Override
    public Booking findNextBooking(Long itemId) {
        return bookingRepository.findAllByItemIdOrderByStartDesc(itemId).stream().findFirst().orElse(null);
    }

    @Override
    public BookingDto findNextBookingToDto(Long itemId) {
        return bookingMapper.toDto(findNextBooking(itemId));
    }

    private List<Booking> findAllBy(boolean isOwner, Long userId, String state,
                                    Integer from, Integer size) {
        User user = userService.findById(userId);
        switch (BookingFilterEnum.of(state)) {
            case ALL:
                return bookingQueryManager.findBy(0, isOwner, user, sort, from, size);
            case CURRENT:
                return bookingQueryManager.findBy(1, isOwner, user, sort, from, size);
            case PAST:
                return bookingQueryManager.findBy(2, isOwner, user, sort, from, size);
            case FUTURE:
                return bookingQueryManager.findBy(3, isOwner, user, sort, from, size);
            case WAITING:
                return bookingQueryManager.findBy(4, isOwner, user, WAITING, sort, from, size);
            case REJECTED:
                return bookingQueryManager.findBy(4, isOwner, user, REJECTED, sort, from, size);
            default:
                throw new ArgumentNotValidException(UNKNOWN_STATE_MESSAGE + state);
        }
    }


    private Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item %s not found.", id)));
    }

}
