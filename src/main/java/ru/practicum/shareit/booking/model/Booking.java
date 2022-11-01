package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.booking.BookingStatusEnum;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Booking {
    @NonNull
    Long id;
    @NonNull
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime start;
    @NonNull
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime end;
    @NonNull
    private Item item;
    @NonNull
    private User booker;
    @NonNull
    private BookingStatusEnum status;
}
