package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ConflictArgumentsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public User create(User user) {
        User createdUser = userRepository.save(user);
        log.debug("{} has been added.", createdUser);
        return createdUser;
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toDto(create(user));
    }

    @Transactional
    @Override
    public User update(User user) {
        User previous = userRepository.findById(user.getId())
                .orElseGet(() -> {
                    log.debug("User {} not found", user.getId());
                    throw new NotFoundException(String.format("User %s not found.", user.getId()));
                });
        if (userRepository.findByEmail(user.getEmail())
                .map(User::getId)
                .filter(id -> !id.equals(user.getId())).isPresent()) {
            log.debug("Email address {} conflict.", user.getEmail());
            throw new ConflictArgumentsException(String.format("Email address %s conflict", user.getEmail()));
        }
        User updateUser = userRepository.save(user);
        log.debug("User updated. Before: {}, after: {}", previous, updateUser);
        return updateUser;
    }

    @Transactional
    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User oldUser = findById(userId);
        return userMapper.toDto(update(userMapper.patchUser(oldUser, userDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found.", userId)));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findByIdToDto(Long userId) {
        return userMapper.toDto(findById(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Transactional
    @Override
    public void remove(Long userId) {
        if (userValidated(userId))
            userRepository.deleteById(userId);
        log.debug("User id {} has been removed.", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAllToDto() {
        return findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean userValidated(Long id) {
        if (!existsById(id)) {
            log.debug("User {} not found", id);
            throw new NotFoundException(String.format("User %s not found.", id));
        }
        return true;
    }
}
