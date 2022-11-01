package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ItemRequest {
    @NonNull
    private Long id;
    @NonNull
    @NotBlank
    private String description;
    @NonNull
    private User requestor;
    @NonNull
    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime created;
}
