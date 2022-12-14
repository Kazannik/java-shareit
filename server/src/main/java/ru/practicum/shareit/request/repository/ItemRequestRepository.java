package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedAsc(Long userId);

    Page<ItemRequest> findAllByRequestorIdOrderByCreatedAsc(Long userId, Pageable pageable);

    List<ItemRequest> findAllByRequestorIsNot(User user);

    Page<ItemRequest> findAllByRequestorIsNot(User user, Pageable pageable);

}
