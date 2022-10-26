package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ConflictArgumentsException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserController controller;
    @Autowired
    private UserService service;

    @BeforeEach
    void beforeEach() {
        controller.createUser(new UserDto("first user", "first@mail.ru"));
        controller.createUser(new UserDto("second user", "second@mail.ru"));
    }

    @AfterEach
    void afterEach() {
        service.clear();
    }

    @Test
    void createUser() {
        controller.createUser(new UserDto("user", "user@mail.ru"));
        Assertions.assertEquals("user@mail.ru",
                service.findUserById(3L).getEmail());
    }

    @Test
    void createUserExistEmail() {
        controller.createUser(new UserDto("user", "user@user.com"));
        final ConflictArgumentsException emailConflictArgumentsException =
                Assertions.assertThrows(ConflictArgumentsException.class,
                () -> service.createUser(new User("user", "user@user.com")));
        Assertions.assertEquals("Email address user@user.com conflict",
                emailConflictArgumentsException.getMessage());
    }

    @Test
    void updateUser() {
        controller.updateUser(new UserDto("user", "user@user.com"), 1L);
        Assertions.assertEquals("user@user.com",
                service.findUserById(1L).getEmail());
    }

    @Test
    void updateUserExistEmail() {
        controller.createUser(new UserDto("user", "user@user.com"));
        final ConflictArgumentsException emailConflictArgumentsException
                = Assertions.assertThrows(ConflictArgumentsException.class,
                () -> controller.updateUser(new UserDto("second user", "user@user.com"), 2L));
        Assertions.assertEquals("Email address user@user.com conflict",
                emailConflictArgumentsException.getMessage());
    }

    @Test
    void findUserById() {
        User user = service.findUserById(2L);
        Assertions.assertEquals("second@mail.ru",
                user.getEmail());
    }

    @Test
    void removeUser() {
        controller.removeUser(1L);
        Assertions.assertEquals(1,
                service.findAll().size());
    }

    @Test
    void findAll() {
        Assertions.assertEquals(2,
                service.findAll().size());
    }
}