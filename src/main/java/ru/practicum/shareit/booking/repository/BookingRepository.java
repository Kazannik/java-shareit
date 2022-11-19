package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemOwner(User owner, Sort sort);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerAndStartAfter(User owner, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerAndStatusEquals(User owner, BookingStatusEnum status, Sort sort);

    Page<Booking> findAllByItemOwner(User owner, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartAfter(User owner, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStatusEquals(User owner, BookingStatusEnum status, Pageable pageable);

    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerAndStatusEquals(User booker, BookingStatusEnum status, Sort sort);

    Page<Booking> findAllByBooker(User booker, Pageable pageable);

    Page<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerAndStatusEquals(User booker, BookingStatusEnum status, Pageable pageable);

    List<Booking> findAllByItemIdOrderByStartAsc(Long itemId);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    Boolean existsAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
            Long userId, Long itemId, BookingStatusEnum status, LocalDateTime created);
}
