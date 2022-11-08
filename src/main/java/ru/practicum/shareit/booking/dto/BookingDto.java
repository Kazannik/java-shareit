package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    private Long id;
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private ItemDto item;
    private UserDto booker;
    private BookingStatusEnum status;
}
