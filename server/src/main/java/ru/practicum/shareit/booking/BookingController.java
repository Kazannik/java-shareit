package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @NotNull @RequestBody BookingDto bookingDto,
                             @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@NotNull @PathVariable Long bookingId,
                              @NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                              @NotNull @RequestParam Boolean approved) {
        return bookingService.approveToDto(bookingId, userId, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                          @NotNull @RequestParam(defaultValue = "ALL") String state,
                                          @RequestParam(required = false) Integer  from,
                                          @RequestParam(required = false) Integer size) {
        return bookingService.findAllByOwnerToDto(userId, state, from, size);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@NotNull @RequestHeader(HEADER_USER_ID) Long userId,
                                         @NotNull @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(required = false) Integer from,
                                         @RequestParam(required = false) Integer size) {
        return bookingService.findAllByUserToDto(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@NotNull @PathVariable Long bookingId,
                              @NotNull @RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.findByIdAndBookerIdToDto(bookingId, userId);
    }
}
