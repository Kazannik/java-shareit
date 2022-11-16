package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.ConflictArgumentsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    private UserDto firstUserDto;
    private UserDto secondUserDto;


    @BeforeAll
    void beforeAll() {
        firstUserDto = new UserDto(1L, "first name", "first@email.com");
        secondUserDto = new UserDto(2L, "second name", "second@email.com");
    }

    @Test
    void createTest() {
        UserDto createdUserDto = userController.create(firstUserDto);
        assertEquals(createdUserDto.getId(), userController.findById(firstUserDto.getId()).getId());
    }

    @Test
    void updateTest() {
        UserDto createdUserDto = userController.create(firstUserDto);
        assertEquals(createdUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
        UserDto updateUserDto = new UserDto(createdUserDto.getId(), "updatename", "update@email.com");
        userController.update(updateUserDto, createdUserDto.getId());
        assertEquals(updateUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
        assertEquals(updateUserDto.getEmail(), userController.findById(createdUserDto.getId()).getEmail());
    }

    @Test
    void updateUserArgumentsExceptionTest() {
        userController.create(firstUserDto);
        UserDto createdUserDto = userController.create(secondUserDto);
        UserDto updateUserDto = new UserDto(createdUserDto.getId(), "update name", firstUserDto.getEmail());
        assertThrows(NotFoundException.class, () -> userController.update(updateUserDto, 77L));
        assertThrows(ConflictArgumentsException.class, () -> userController.update(updateUserDto, 2L));
        assertThrows(NotFoundException.class, () -> userService
                .update(new User(7L, "name", "email7@email.com")));
    }

    @Test
    void findByIdTest() {
        UserDto createdUserDto = userController.create(firstUserDto);
        assertEquals(createdUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
    }

    @Test
    void removeTest() {
        UserDto createdUserDto = userController.create(firstUserDto);
        int size = userController.findAll().size();
        userController.remove((createdUserDto.getId()));
        assertEquals(size - 1, userController.findAll().size());
    }

    @Test
    void findAllTest() {
        assertEquals(0, userController.findAll().size());
    }

    @Test
    void userIdNotFoundExceptionTest() {
        assertThrows(NotFoundException.class, () -> userController.remove(77L));
    }


}