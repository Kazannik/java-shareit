package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemControllerTest {
    @Autowired
    private ItemController itemController;
    @Autowired
    private UserController userController;
    @Autowired
    private BookingController bookingController;
    private ItemDto firstItemDto;
    private ItemDto secondItemDto;
    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private CommentDto comment;
    BookingDto booking;

    @BeforeAll
    void beforeAll() {
        firstItemDto = new ItemDto(1L, "first item name", "description", true);
        secondItemDto = new ItemDto(2L, "second item name", "description", true);
        firstUserDto = new UserDto(1L, "first username", "first@email.com");
        secondUserDto = new UserDto(2L, "second username", "second@email.com");
        comment = new CommentDto(1L, "text", "author");
        booking = new BookingDto(1L,
                LocalDateTime.of(2022, 11, 14, 20, 0),
                LocalDateTime.of(2022, 11, 14, 20, 5),
                firstUserDto.getId(), secondUserDto.getId());
    }

    @Test
    void createItemTest() {
        UserDto user = userController.create(firstUserDto);
        ItemDto item = itemController.create(firstItemDto, 1L);
        assertEquals(item.getId(), itemController.findById(item.getId(), user.getId()).getId());
    }

    @Test
    void updateItemTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        itemController.update(secondItemDto, 1L, 1L);
        assertEquals(secondItemDto.getName(), itemController.findById(1L, 1L).getName());
    }

    @Test
    void findByIdTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertEquals(firstItemDto.getName(), itemController.findById(1L, 1L).getName());
    }

    @Test
    void findAllTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertEquals(1, itemController.findAll(1L, null, null).size());
    }

    @Test
    void searchTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertEquals(1, itemController.search(1L, "ScRiPt", null, null).size());
    }

    @Test
    void searchNotFoundTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertEquals(0, itemController.search(1L, "cRiPtO", null, null).size());
    }

    @Test
    void createCommentTest() {
        userController.create(firstUserDto);
        ItemDto item = itemController.create(firstItemDto, 1L);
        UserDto secondUser = userController.create(secondUserDto);
        bookingController.create(booking, secondUser.getId());
        bookingController.approve(1L, 1L, true);
        itemController.create(item.getId(), secondUser.getId(), comment);
        assertEquals(1, itemController.findById(1L, 1L).getComments().size());
    }

    @Test
    void paginationTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertEquals(1, itemController.findAll(1L, 1, 5).size());
    }

    @Test
    void paginationArgumentNotValidTest() {
        userController.create(firstUserDto);
        itemController.create(firstItemDto, 1L);
        assertThrows(ArgumentNotValidException.class, () -> itemController.findAll(1L, -1, 0));
    }
}