package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @NotNull @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @NotNull @RequestBody UserDto userDto,
                          @NotNull @PathVariable Long userId) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@NotNull @PathVariable Long userId) {
        return userService.findByIdToDto(userId);
    }

    @DeleteMapping("/{userId}")
    public void remove(@NotNull @PathVariable Long userId) {
        userService.remove(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAllToDto();
    }
}
