package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    Long id;
    @NonNull
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;

    public CommentDto(Long id, @NonNull String text, String authorName) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
    }
}
