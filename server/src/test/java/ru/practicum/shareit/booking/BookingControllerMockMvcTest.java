package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerMockMvcTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private final UserDto userDto = new UserDto(1L,"name", "email@email.com");
    private final ItemDto itemDto = new ItemDto(2L,"name", "description", true);
    private final BookingDto bookingDto = new BookingDto(3L,
            LocalDateTime.of(2025, 11, 14, 18, 0),
            LocalDateTime.of(2025, 11, 14, 18, 10),
            2L, 1L, itemDto, userDto, BookingStatusEnum.APPROVED);

    @Test
    void createTest() throws Exception {
        when(bookingService.create(anyLong(),any()))
                .thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void approveTest() throws Exception {
        when(bookingService.approveToDto(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getAllByOwner() throws Exception {
        when(bookingService.findAllByOwnerToDto(anyLong(), anyString(), eq(null), eq(null)))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(bookingService.findAllByUserToDto(anyLong(), anyString(), eq(null), eq(null)))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.findByIdAndBookerIdToDto(anyLong(), anyLong()))
                .thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }
}