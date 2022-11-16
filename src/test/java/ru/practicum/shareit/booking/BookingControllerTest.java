package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.enums.BookingStatusEnum.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;
    @Autowired
    private UserController userController;
    @Autowired
    private ItemController itemController;
    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private ItemDto availableItemDto;
    private ItemDto notAvailableItemDto;
    private BookingDto bookingDto;
    private BookingDto notAvailableBookingDto;
    private BookingDto notValidPeriodBookingDto;
    private UserDto ownerUserDto;
    private UserDto bookerUserDto;
    private BookingDto createdBookingDto;

    @BeforeAll
    void beforeAll() {
        firstUserDto = new UserDto(1L, "first user name", "first_user@email.com");
        secondUserDto = new UserDto(2L, "second user name", "second_user@email.com");
        availableItemDto = new ItemDto(1L, "item name", "item description", true);
        notAvailableItemDto = new ItemDto(2L, "Not Available Item Name", "item description",
                false);
        bookingDto = new BookingDto(1L,
                LocalDateTime.of(2023, 11, 14, 20, 10),
                LocalDateTime.of(2025, 11, 14, 20, 10), 1L, 2L);
        notAvailableBookingDto = new BookingDto(1L,
                LocalDateTime.of(2023, 11, 14, 20, 10),
                LocalDateTime.of(2025, 11, 14, 20, 10), 2L, 2L);
        notValidPeriodBookingDto = new BookingDto(1L,
                LocalDateTime.of(2025, 11, 14, 20, 10),
                LocalDateTime.of(2022, 11, 14, 20, 10), 1L, 2L);

    }

    @BeforeEach
    void beforeEach() {
        ownerUserDto = userController.create(firstUserDto);
        itemController.create(availableItemDto, ownerUserDto.getId());
        itemController.create(notAvailableItemDto, ownerUserDto.getId());
        bookerUserDto = userController.create(secondUserDto);
        createdBookingDto = bookingController.create(bookingDto, bookerUserDto.getId());
    }

    @Test
    void createTest() {
        assertEquals(1L, bookingController.getById(createdBookingDto.getId(), ownerUserDto.getId()).getId());
    }

    @Test
    void createBookingExceptionTest() {
        assertThrows(NotFoundException.class, () -> bookingController
                .getById(77L, ownerUserDto.getId()));
        assertThrows(NotFoundException.class, () -> bookingController
                .getById(createdBookingDto.getId(), 77L));
        assertThrows(NotFoundException.class, () -> bookingController
                .create(new BookingDto(1L,
                        LocalDateTime.of(2023, 11, 14, 20, 10),
                        LocalDateTime.of(2025, 11, 14, 20, 10),
                        1L, 1L), 1L));
        itemController.create(notAvailableItemDto, ownerUserDto.getId());
        assertThrows(ArgumentNotValidException.class, () -> bookingController
                .create(notAvailableBookingDto, bookerUserDto.getId()));
        assertThrows(ArgumentNotValidException.class, () -> bookingController
                .create(notValidPeriodBookingDto, bookerUserDto.getId()));
    }

    @Test
    void createBookingNotFoundExceptionTest() {
        assertThrows(NotFoundException.class, () -> bookingController
                .getById(77L, ownerUserDto.getId()));
        assertThrows(NotFoundException.class, () -> bookingController
                .getById(createdBookingDto.getId(), 77L));
    }

    @Test
    void notApproveTest() {
        assertEquals(WAITING, bookingController.getById(createdBookingDto.getId(), ownerUserDto.getId()).getStatus());
        bookingController.approve(createdBookingDto.getId(), ownerUserDto.getId(), false);
        assertEquals(REJECTED, bookingController.getById(createdBookingDto.getId(), ownerUserDto.getId()).getStatus());
    }

    @Test
    void approveTest() {
        assertEquals(WAITING, bookingController.getById(createdBookingDto.getId(), ownerUserDto.getId()).getStatus());
        bookingController.approve(createdBookingDto.getId(), ownerUserDto.getId(), true);
        assertEquals(APPROVED, bookingController.getById(createdBookingDto.getId(), ownerUserDto.getId()).getStatus());
    }

    @Test
    void approveExceptionTest() {
        assertThrows(NotFoundException.class, () -> bookingController
                .approve(createdBookingDto.getId(), bookerUserDto.getId(), true));
        bookingController.approve(createdBookingDto.getId(), ownerUserDto.getId(), true);
        assertThrows(ArgumentNotValidException.class, () -> bookingController
                .approve(createdBookingDto.getId(), ownerUserDto.getId(), true));
    }

    @Test
    void getAllByOwnerTest() {
        assertEquals(1, bookingController.getAllByOwner(1L, "ALL", null, null).size());
        assertThrows(NotFoundException.class, () -> bookingController
                .getAllByOwner(33L, "ALL", null, null));
    }

    @Test
    void getAllByUserTest() {
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "ALL", null, null).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "CURRENT", null, null).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "PAST", null, null).size());
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "FUTURE", null, null).size());
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "WAITING", null, null).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "REJECTED", null, null).size());

        bookingController.approve(createdBookingDto.getId(), ownerUserDto.getId(), true);
        assertEquals(1, bookingController
                .getAllByOwner(ownerUserDto.getId(), "ALL", null, null).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "CURRENT", null, null).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "PAST", null, null).size());
        assertEquals(1, bookingController
                .getAllByOwner(ownerUserDto.getId(), "FUTURE", null, null).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "WAITING", null, null).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "REJECTED", null, null).size());
    }

    @Test
    void getAllByUserPaginationTest() {
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "ALL", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "CURRENT", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "PAST", 1, 5).size());
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "FUTURE", 1, 5).size());
        assertEquals(1, bookingController
                .getAllByUser(bookerUserDto.getId(), "WAITING", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByUser(bookerUserDto.getId(), "REJECTED", 1, 5).size());

        bookingController.approve(createdBookingDto.getId(), ownerUserDto.getId(), true);
        assertEquals(1, bookingController
                .getAllByOwner(ownerUserDto.getId(), "ALL", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "CURRENT", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "PAST", 1, 5).size());
        assertEquals(1, bookingController
                .getAllByOwner(ownerUserDto.getId(), "FUTURE", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "WAITING", 1, 5).size());
        assertEquals(0, bookingController
                .getAllByOwner(ownerUserDto.getId(), "REJECTED", 1, 5).size());

        assertThrows(ArgumentNotValidException.class, () -> bookingController
                .getAllByOwner(ownerUserDto.getId(), "BLA-BLA-BLA", 1, 5));
    }

    @Test
    void getByIdTest() {
        assertEquals(createdBookingDto.getId(), bookingController
                .getById(createdBookingDto.getId(), ownerUserDto.getId()).getId());
    }

    @Test
    void paginationTest() {
        assertEquals(1, bookingController
                .getAllByOwner(1L, "ALL", 1, 5).size());
    }

    @Test
    void paginationArgumentNotValidTest() {
        assertThrows(ArgumentNotValidException.class, () -> bookingController
                .getAllByOwner(1L, "ALL", -1, 0));
    }
}