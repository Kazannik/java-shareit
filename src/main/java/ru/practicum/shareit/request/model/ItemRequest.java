package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequest {
    @NonNull
    Long id;
    @NonNull
    @NotBlank
    String description;
    @NonNull
    User requestor;
    @NonNull
    @JsonFormat(pattern="YYYY-MM-DDTHH:mm:ss")
    LocalDateTime created;
}
