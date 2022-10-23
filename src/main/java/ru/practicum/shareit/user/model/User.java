package ru.practicum.shareit.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Setter
    Long id;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @NotBlank
    @Email
    String email;
}
