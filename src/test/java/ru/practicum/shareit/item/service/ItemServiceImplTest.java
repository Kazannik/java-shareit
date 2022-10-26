package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.AccessForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemController controller;
    @Autowired
    private ItemService service;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userController.createUser(new UserDto("Иванов И.И.", "ivanov@mail.com"));
        userController.createUser(new UserDto("Петров П.П.", "petrov@mail.com"));
        controller.createItem(
                new ItemDto(1L, "Дрель", "Аккумуляторная дрель", true, 0L), 1L);
        controller.createItem(
                new ItemDto(2L, "Паяльник", "Паяльная станция", true, 0L), 1L);
    }

    @AfterEach
    void afterEach() {
        service.clear();
        userService.clear();
    }

    @Test
    void createItem() {
        controller.createItem(
                new ItemDto(3L, "Карты", "Колода карт", true, 0L), 2L);
        Assertions.assertEquals(2,
                service.findAll(1L).size());
    }

    @Test
    void createUserNotFount() {
        final NotFoundException userNotFoundException
                = Assertions.assertThrows(NotFoundException.class,
                () -> service.createItem(new Item("Карты", "Колода карт", true, 3L)));
        Assertions.assertEquals("User 3 not found.",
                userNotFoundException.getMessage());
    }

    @Test
    void updateItem() {
        controller.createItem(
                new ItemDto(3L, "Карты", "Колода карт", true, 0L), 1L);
        controller.updateItem(new ItemDto(2L, "Паяльник + фен", "Паяльная станция",
                        true, 0L),
                2L, 1L);
        Assertions.assertEquals("Паяльник + фен",
                service.findItemById(2L).getName());
    }

    @Test
    void updateItemBadName() {
        final NullPointerException nameNullPointerException
                = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateItem(new ItemDto(2L, "", "Паяльная станция",
                                true, 0L),
                        2L, 1L));
        Assertions.assertEquals("Item 1 name not correct.",
                nameNullPointerException.getMessage());
    }

    @Test
    void updateItemBadDescription() {
        final NullPointerException descriptionNullPointerException
                = Assertions.assertThrows(NullPointerException.class,
                () -> controller.updateItem(new ItemDto(2L, "Паяльник + фен", "",
                                true, 0L),
                        2L, 1L));
        Assertions.assertEquals("Item 1 description not correct.",
                descriptionNullPointerException.getMessage());
    }

    @Test
    void updateItemUserAccessForbidden() {
        final AccessForbiddenException userAccessForbiddenException
                = Assertions.assertThrows(AccessForbiddenException.class,
                () -> controller.updateItem(new ItemDto(2L, "Паяльник + фен", "Паяльная станция",
                                true, 0L),
                        2L, 2L));
        Assertions.assertEquals("User 2 access to the item 2 is forbidden.",
                userAccessForbiddenException.getMessage());
    }

    @Test
    void findItemById() {
        controller.createItem(
                new ItemDto(3L, "Карты", "Колода карт", true, 0L), 2L);
        Assertions.assertEquals("Паяльник",
                service.findItemById(2L).getName());
    }

    @Test
    void findAll() {
        controller.createItem(
                new ItemDto(3L, "Карты", "Колода карт", true, 0L), 2L);
        Assertions.assertEquals(2,
                service.findAll(1L).size());
    }

    @Test
    void search() {
        controller.createItem(
                new ItemDto(3L, "Карты", "Колода карт", true, 0L), 2L);
        Assertions.assertEquals(2,
                service.search("лЬ").size());
    }
}
