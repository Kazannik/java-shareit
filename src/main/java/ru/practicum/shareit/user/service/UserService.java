package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    boolean existsUserById(Long id);

    boolean existsUserByEmail(String email);

    User findUserById(Long id);

    void removeUser(Long id);

    List<User> findAll();

    void clear();

}
