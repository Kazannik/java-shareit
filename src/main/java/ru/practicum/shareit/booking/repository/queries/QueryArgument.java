package ru.practicum.shareit.booking.repository.queries;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.user.model.User;

@Getter
class QueryArgument {
    private final BookingRepository repository;
    @Setter
    private User owner;
    @Setter
    private User booker;
    @Setter
    private BookingStatusEnum status;
    @Setter
    private Sort sort;
    @Setter
    private Pageable pageable;

    public QueryArgument(BookingRepository repository) {
        this.repository = repository;
    }
}
