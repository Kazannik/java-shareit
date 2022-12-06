package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @NotNull @RequestBody UserRequestDto requestDto) {
        log.info("Creating user {}", requestDto);
        return userClient.createUser(requestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Valid @NotNull @RequestBody UserRequestDto requestDto,
                                         @NotNull @PathVariable Long userId) {
        log.info("Update user {}, userId={}", requestDto, userId);
        return userClient.updateItem(userId, requestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@NotNull @PathVariable long userId) {
        log.info("Get user by Id, userId={}", userId);
        return userClient.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> remove(@NotNull @PathVariable long userId) {
        log.info("Remove user, userId={}", userId);
        return userClient.remove(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Find all users");
        return userClient.getAll();
    }
}
