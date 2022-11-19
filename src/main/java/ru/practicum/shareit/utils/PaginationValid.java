package ru.practicum.shareit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exceptions.ArgumentNotValidException;

import java.util.Optional;

@Slf4j
public class PaginationValid {

    public static Optional<PageRequest> pageValidated(Integer from, Integer size) {
        if (from != null && size != null) {
            if (from < 0) {
                log.debug("Row index {} must not be less than zero", from);
                throw new ArgumentNotValidException(
                        String.format("Row index %s must not be less than zero.", from));
            } else if (size < 1) {
                log.debug("Size {} of the page to be returned, must be greater than zero", size);
                throw new ArgumentNotValidException(
                        String.format("Size %s of the page to be returned, must be greater than zero.", size));
            } else {
                return Optional.of(PageRequest.of(from / size, size));
            }
        } else {
            return Optional.empty();
        }
    }
}
