package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
public class Item {

    public Item(String name, String description, boolean available, Long owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    @Setter
    private Long id;
    @NonNull
    @NotBlank
    private final String name;
    @NonNull
    @NotBlank
    private final String description;
    private final boolean available;
    @NonNull
    private final Long owner;
    @Setter
    private ItemRequest request;

    public boolean isAvailable() {
        return available;
    }
}
