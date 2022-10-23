package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(UserDto dto) {
        User user = new User(dto.getName(), dto.getEmail());
        user.setId(dto.getId());
        return user;
    }

    @Override
    public User patchUser(User user, UserDto dto) {
        User patchedUser = new User(dto.getName() != null ? dto.getName() : user.getName(),
                dto.getEmail() != null ? dto.getEmail() : user.getEmail());
        patchedUser.setId(user.getId());
        return patchedUser;
    }

    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
