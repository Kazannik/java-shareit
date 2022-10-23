package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictArgumentsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        if (userStorage.existsUserByEmail(user.getEmail())) {
            log.debug("Email address {} conflict.", user.getEmail());
            throw new ConflictArgumentsException(String.format("Email address %s conflict", user.getEmail()));
        }
        User createdUser = userStorage.createUser(user);
        log.debug("{} has been added.", createdUser);
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        if (!userStorage.existsUserById(user.getId())) {
            log.debug("User {} not found", user.getId());
            throw new NotFoundException(String.format("User %s not found.", user.getId()));
        }
        if (userStorage.existsUserByEmail(user.getEmail())
                && !userStorage.findUserByEmail(user.getEmail()).get()
                .getId().equals(user.getId())) {
            log.debug("Email address {} conflict.", user.getEmail());
            throw new ConflictArgumentsException(String.format("Email address %s conflict", user.getEmail()));
        }
        User previous = userStorage.findUserById(user.getId()).get();
        User updateUser = userStorage.updateUser(user);
        log.debug("User updated. Before: {}, after: {}", previous, updateUser);
        return updateUser;
    }

    @Override
    public User findUserById(Long id) {
        return userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found.", id)));
    }

    @Override
    public boolean existsUserById(Long id) {
        return userStorage.existsUserById(id);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userStorage.existsUserByEmail(email);
    }

    @Override
    public void removeUser(Long id) {
        if (!userStorage.existsUserById(id)) {
            log.debug("User {} not found", id);
            throw new NotFoundException(String.format("User %s not found.", id));
        }
        userStorage.deleteById(id);
        log.debug("User id {} has been removed.", id);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public void clear() {
        userStorage.clear();
    }
}
