package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    @NonNull
    Long id;
    @NonNull
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    LocalDateTime start;
    @NonNull
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    LocalDateTime end;
    @NonNull
    Item item;
    @NonNull
    User booker;
    @NonNull
    BookingStatusEnum status;
}
