package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRequestControllerTest {
    @Autowired
    private ItemRequestController itemRequestController;
    @Autowired
    private UserController userController;
    private ItemRequestDto itemRequestDto;
    private UserDto firstUserDto;
    private UserDto secondUserDto;

    @BeforeAll
    void beforeAll() {
        itemRequestDto = new ItemRequestDto(1L, "request description");
        firstUserDto = new UserDto(1L, "first name", "first@email.com");
        secondUserDto = new UserDto(2L, "second name", "second@email.com");
    }

    @Test
    void createTest() {
        UserDto user = userController.create(firstUserDto);
        ItemRequestDto itemRequest = itemRequestController.create(itemRequestDto, user.getId());
        assertEquals(1L, itemRequestController.getById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(firstUserDto);
        itemRequestController.create(itemRequestDto, user.getId());
        assertEquals(1, itemRequestController.getAllByUser(user.getId(), null, null).size());
    }

    @Test
    void getAllTest() {
        UserDto firstUser = userController.create(firstUserDto);
        itemRequestController.create(itemRequestDto, firstUser.getId());
        assertEquals(0, itemRequestController.getAll(firstUser.getId(), null, null).size());
        UserDto secondUser = userController.create(secondUserDto);
        assertEquals(1, itemRequestController.getAll(secondUser.getId(), null, null).size());
    }

    @Test
    void getByIdTest() {
        UserDto user = userController.create(firstUserDto);
        itemRequestController.create(itemRequestDto, user.getId());
        assertEquals("request description", itemRequestController.getById(1L, user.getId()).getDescription());
    }

    @Test
    void paginationTest() {
        UserDto firstUser = userController.create(firstUserDto);
        itemRequestController.create(itemRequestDto, firstUser.getId());
        UserDto secondUser = userController.create(secondUserDto);
        assertEquals(1, itemRequestController.getAll(secondUser.getId(), null, null).size());
    }

    @Test
    void paginationArgumentNotValidTest() {
        UserDto firstUser = userController.create(firstUserDto);
        itemRequestController.create(itemRequestDto, firstUser.getId());
        UserDto secondUser = userController.create(secondUserDto);
        assertThrows(ArgumentNotValidException.class, () -> itemRequestController.getAll(secondUser.getId(), -1, 0));
    }
}