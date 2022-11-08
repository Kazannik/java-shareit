package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    UserDto create(UserDto userDto);

    User update(User user);

    UserDto update(Long userId, UserDto userDto);

    boolean existsById(Long userId);

    User findById(Long userId);

    UserDto findByIdToDto(Long id);

    void remove(Long id);

    List<User> findAll();

    List<UserDto> findAllToDto();
}
