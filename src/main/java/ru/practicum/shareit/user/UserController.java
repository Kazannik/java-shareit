package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @NotNull @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @NotNull @RequestBody UserDto userDto,
                              @Valid @NotNull @PathVariable Long userId) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@Valid @NotNull @PathVariable Long userId) {
        return userService.findByIdToDto(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@Valid @NotNull @PathVariable Long userId) {
        userService.remove(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAllToDto();
    }
}
