package ru.practicum.shareit.booking.repository.queries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class BookingQueryManager {
    private final BookingRepository bookingRepository;
    private final List<Function<QueryArgument, List<Booking>>> listQueries;

    @Autowired
    public BookingQueryManager(BookingRepository bookingRepository,
                               List<Function<QueryArgument, List<Booking>>> listQueries) {
        this.bookingRepository = bookingRepository;
        this.listQueries = Arrays.asList(
                BookingQueryManager::f1,
                BookingQueryManager::f2,
                BookingQueryManager::f3,
                BookingQueryManager::f4,
                BookingQueryManager::f5,
                BookingQueryManager::f6,
                BookingQueryManager::f7,
                BookingQueryManager::f8,
                BookingQueryManager::f9,
                BookingQueryManager::f10,
                BookingQueryManager::f11,
                BookingQueryManager::f12,
                BookingQueryManager::f13,
                BookingQueryManager::f14,
                BookingQueryManager::f15,
                BookingQueryManager::f16,
                BookingQueryManager::f17,
                BookingQueryManager::f18,
                BookingQueryManager::f19,
                BookingQueryManager::f20);
    }

    public List<Booking> findBy(int index, boolean byOwner, User user, BookingStatusEnum status, Sort sort,
                                Integer from, Integer size) {
        QueryArgument queryArgument = new QueryArgument(bookingRepository);
        queryArgument.setStatus(status);
        return findBy(queryArgument, index, byOwner, user, sort, from, size);
    }

    public List<Booking> findBy(int index, boolean byOwner, User user, Sort sort,
                                Integer from, Integer size) {
        return findBy(new QueryArgument(bookingRepository), index, byOwner, user, sort, from, size);
    }

    private List<Booking> findBy(QueryArgument arg, int index, boolean byOwner, User user, Sort sort,
                                 Integer from, Integer size) {
        if (byOwner) {
            arg.setOwner(user);
        } else {
            index += 10;
            arg.setBooker(user);
        }
        if (pageValidated(from, size)) {
            index += 5;
            arg.setPageable(PageRequest.of(from / size, size, sort));
        } else {
            arg.setSort(sort);
        }
        return listQueries.get(index).apply(arg);
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

    private static List<Booking> f1(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwner(arg.getOwner(), arg.getSort());
    }

    private static List<Booking> f2(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStartBeforeAndEndAfter(arg.getOwner(),
                LocalDateTime.now(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f3(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndEndBefore(arg.getOwner(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f4(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStartAfter(arg.getOwner(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f5(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStatusEquals(arg.getOwner(), arg.getStatus(), arg.getSort());
    }

    private static List<Booking> f6(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwner(arg.getOwner(), arg.getPageable()).toList();
    }

    private static List<Booking> f7(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStartBeforeAndEndAfter(
                arg.getOwner(), LocalDateTime.now(), LocalDateTime.now(), arg.getPageable()).toList();
    }

    private static List<Booking> f8(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndEndBefore(arg.getOwner(), LocalDateTime.now(),
                arg.getPageable()).toList();
    }

    private static List<Booking> f9(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStartAfter(arg.getOwner(), LocalDateTime.now(),
                arg.getPageable()).toList();
    }

    private static List<Booking> f10(QueryArgument arg) {
        return arg.getRepository().findAllByItemOwnerAndStatusEquals(arg.getOwner(), arg.getStatus(), arg.getPageable())
                .toList();
    }

    private static List<Booking> f11(QueryArgument arg) {
        return arg.getRepository().findAllByBooker(arg.getBooker(), arg.getSort());
    }

    private static List<Booking> f12(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStartBeforeAndEndAfter(arg.getBooker(),
                LocalDateTime.now(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f13(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndEndBefore(arg.getBooker(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f14(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStartAfter(arg.getBooker(), LocalDateTime.now(), arg.getSort());
    }

    private static List<Booking> f15(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStatusEquals(arg.getBooker(), arg.getStatus(), arg.getSort());
    }

    private static List<Booking> f16(QueryArgument arg) {
        return arg.getRepository().findAllByBooker(arg.getBooker(), arg.getPageable()).toList();
    }

    private static List<Booking> f17(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStartBeforeAndEndAfter(
                arg.getBooker(), LocalDateTime.now(), LocalDateTime.now(), arg.getPageable()).toList();
    }

    private static List<Booking> f18(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndEndBefore(arg.getBooker(), LocalDateTime.now(), arg.getPageable())
                .toList();
    }

    private static List<Booking> f19(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStartAfter(arg.getBooker(), LocalDateTime.now(), arg.getPageable())
                .toList();
    }

    private static List<Booking> f20(QueryArgument arg) {
        return arg.getRepository().findAllByBookerAndStatusEquals(arg.getBooker(), arg.getStatus(), arg.getPageable())
                .toList();
    }
}
