package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryDataJpaTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void beforeAll() {
        User user = userRepository.save(new User(1L,"name", "email@email.com"));
        itemRepository.save(new Item("name", "description", true, user));
    }

    @Test
    void searchTextIgnoreCaseTest() {
        List<Item> items = itemRepository.searchTextIgnoreCase("ScripT");
        assertEquals(1, items.size());
    }

    @Test
    void searchTextIgnoreCasePageableTest() {
        Page<Item> items = itemRepository.searchTextIgnoreCase("ScripT", Pageable.ofSize(5));
        assertEquals(1, items.toList().size());
    }

    @Test
    void notSearchTextIgnoreCaseTest() {
        List<Item> items = itemRepository.searchTextIgnoreCase("Cripto");
        assertEquals(0, items.size());
    }

    @Test
    void notSearchTextIgnoreCasePageableTest() {
        Page<Item> items = itemRepository.searchTextIgnoreCase("Cripto", Pageable.ofSize(5));
        assertEquals(0, items.toList().size());
    }
}