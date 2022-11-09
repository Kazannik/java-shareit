package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper
public interface CommentMapper {

    @Mapping(source = "comment.author.name", target = "authorName")
    CommentDto toDto(Comment comment);

    Comment toComment(CommentDto dto);

}
