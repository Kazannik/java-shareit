package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDto {
    private Long id;
    @NotNull
    @NotBlank
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    public ItemRequestDto(Long id, String description, LocalDateTime created, List<ItemDto> items) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = items;
    }

    public ItemRequestDto(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
