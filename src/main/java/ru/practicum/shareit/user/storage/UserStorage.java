package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    boolean existsUserById(Long id);

    boolean existsUserByEmail(String email);

    void deleteById(Long id);

    List<User> findAll();

    void clear();
}
