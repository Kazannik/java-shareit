package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerMockMvcTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto(1L, "description");
    }

    @Test
    void createTest() throws Exception {
        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(itemRequestService.getAllByUserToDto(anyLong(), eq(null), eq(null)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto)), true));
    }

    @Test
    void getAllTest() throws Exception {
        when(itemRequestService.getAllToDto(anyLong(), eq(null), eq(null)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemRequestService.getByIdToDto(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }
}