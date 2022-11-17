package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDto {
    Long id;
    @NotNull
    @NotBlank
    String description;
    LocalDateTime created;
    List<ItemDto> items;

    public ItemRequestDto(Long id, String description, LocalDateTime created, List<ItemDto> items) {
        this(id, description);
        this.created = created;
        this.items = items;
    }

    public ItemRequestDto(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
