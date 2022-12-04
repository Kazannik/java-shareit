package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    @NotBlank
    @Column
    String name;
    @NonNull
    @NotBlank
    @Column
    String description;
    @NonNull
    @Column(name = "is_available")
    boolean available;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    User owner;
    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    ItemRequest request;
}
