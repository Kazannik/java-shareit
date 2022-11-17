package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper
public interface UserMapper {

    User toUser(UserDto dto);

    UserDto toDto(User user);

    default User patchUser(User user, UserDto dto) {
        User patchedUser = new User(user.getId(), dto.getName() != null ? dto.getName() : user.getName(),
                dto.getEmail() != null ? dto.getEmail() : user.getEmail());
        return patchedUser;
    }
}
