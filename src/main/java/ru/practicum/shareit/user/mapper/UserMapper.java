package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper
public interface UserMapper {

    User toUser(UserDto dto);

    User patchUser(User user, UserDto dto);

    UserDto toUserDto(User user);

}
