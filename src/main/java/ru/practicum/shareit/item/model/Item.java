package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    @Setter
    Long id;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @NotBlank
    String description;
    @NonNull
    Boolean available;
    @NonNull
    Long owner;
    @Setter
    ItemRequest request;
}
