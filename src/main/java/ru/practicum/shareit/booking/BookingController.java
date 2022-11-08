package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @NotNull @RequestBody BookingDto bookingDto,
                             @Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@Valid @NotNull @PathVariable Long bookingId,
                              @Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                              @Valid @NotNull @RequestParam Boolean approved) {
        return bookingService.approveToDto(bookingId, userId, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                          @Valid @NotNull @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByOwnerToDto(userId, state);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                         @Valid @NotNull @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByUserToDto(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@Valid @NotNull @PathVariable Long bookingId,
                              @Valid @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.findByIdAndBookerIdToDto(bookingId, userId);
    }
}
