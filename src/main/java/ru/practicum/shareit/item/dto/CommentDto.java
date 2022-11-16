package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Long id;
    @NonNull
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(Long id, @NonNull String text, String authorName) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
    }
}
