package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private UserController userController;
    private UserDto userDto;

    @BeforeAll
    void beforeAll() {
        userDto = new UserDto(1L, "name", "email@email.com");
    }

    @Test
    void createTest() {
        UserDto createdUserDto = userController.create(userDto);
        assertEquals(createdUserDto.getId(), userController.findById(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        UserDto createdUserDto = userController.create(userDto);
        assertEquals(createdUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
        UserDto updateUserDto = new UserDto(createdUserDto.getId(), "updatename", "update@email.com");
        userController.update(updateUserDto, createdUserDto.getId());
        assertEquals(updateUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
        assertEquals(updateUserDto.getEmail(), userController.findById(createdUserDto.getId()).getEmail());
    }

    @Test
    void findByIdTest() {
        UserDto createdUserDto = userController.create(userDto);
        assertEquals(createdUserDto.getName(), userController.findById(createdUserDto.getId()).getName());
    }

    @Test
    void removeTest() {
        UserDto createdUserDto = userController.create(userDto);
        int size =  userController.findAll().size();
        userController.remove((createdUserDto.getId()));
        assertEquals(size - 1, userController.findAll().size());
    }

    @Test
    void findAllTest() {
        assertEquals(0, userController.findAll().size());
    }
}