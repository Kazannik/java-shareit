package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "items")
public class Item {
    public Item(@NonNull String name, @NonNull String description, boolean available, @NonNull User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @NotBlank
    @Column
    private String name;
    @NonNull
    @NotBlank
    @Column
    private String description;
    @Column(name = "is_available")
    private boolean available;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "id_owner", referencedColumnName = "id", nullable = false)
    private User owner;
    @Setter
    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;

    public boolean isAvailable() {
        return available;
    }
}
