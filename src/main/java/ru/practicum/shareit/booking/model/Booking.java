package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
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
public class Booking {
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
    String status;
}
