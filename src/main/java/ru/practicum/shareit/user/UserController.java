package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @NotNull @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user = userService.createUser(user);
        return userMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @NotNull @RequestBody UserDto userDto, @PathVariable Long userId) {
        User oldUser = userService.findUserById(userId);
        User user = userService.updateUser(userMapper.patchUser(oldUser, userDto));
        return userMapper.toUserDto(user);
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return userMapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
