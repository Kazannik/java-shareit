package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository()
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;

    private long key = 1;

    private long nextIdGenerator() {
        return key++;
    }

    private void generatorReset() {
        key = 1;
    }

    @Override
    public User createUser(User user) {
        user = new User(user.getName(), user.getEmail());
        user.setId(nextIdGenerator());
        users.put(user.getId(), user);
        log.debug("{} has been added.", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.debug("User {} updated.", user);
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        User user = users.get(id);
        if (user != null) {
            log.debug("Search result: {}", user);
            return Optional.of(user);
        } else {
            log.debug("User {} not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst();
    }

    @Override
    public boolean existsUserById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return users.values().stream().anyMatch(u -> email.equals(u.getEmail()));
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void clear() {
        users.clear();
        generatorReset();
    }
}
