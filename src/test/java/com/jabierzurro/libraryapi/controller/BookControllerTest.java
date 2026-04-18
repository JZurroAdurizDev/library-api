package com.jabierzurro.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import com.jabierzurro.libraryapi.service.BookService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /books should return book list")
    void getAllBooksShouldReturnOk() throws Exception {

        List<BookResponseDTO> books = List.of(
            new BookResponseDTO(1, "Dummy", "Dawson", "0-7414-9349-7", (short) 2025, 200)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookId").value(1));
    }

    @Test
    @DisplayName("GET /books/{id} should return book when it exists")
    void getBookByIdShouldReturnOk() throws Exception {

        BookResponseDTO book = new BookResponseDTO(
            1, "Dummy", "Dawson", "0-7414-9349-7", (short) 2025, 200
        );

        when(bookService.getBookById(1)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.isbn").value("0-7414-9349-7"));
    }

    @Test
    @DisplayName("GET /books/search should return filtered books")
    void searchBooksShouldReturnOk() throws Exception {

        List<BookResponseDTO> books = List.of(
            new BookResponseDTO(1, "Dummy", "Dawson", "0-7414-9349-7", (short) 2025, 200)
        );

        when(bookService.search("Dummy", null, null, null)).thenReturn(books);

        mockMvc.perform(get("/books/search")
                .param("title", "Dummy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].title").value("Dummy"));
    }

    @Test
    @DisplayName("POST /books should create book and return 201")
    void createBookShouldReturnCreated() throws Exception {

        BookRequestDTO request = new BookRequestDTO(
            "Dummy",
            "Dawson",
            "0-7414-9349-7",
            (short) 2025,
            200
        );

        BookResponseDTO response = new BookResponseDTO(
            1,
            "Dummy",
            "Dawson",
            "0-7414-9349-7",
            (short) 2025,
            200
        );

        when(bookService.create(any(BookRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.title").value("Dummy"));
    }

    @Test
    @DisplayName("PUT /books/{id} should update book and return 200")
    void updateBookShouldReturnOk() throws Exception {

        BookRequestDTO request = new BookRequestDTO(
            "Dummy",
            "Dawson",
            "0-7414-9349-7",
            (short) 2025,
            200
        );

        BookResponseDTO response = new BookResponseDTO(
            1,
            "Dummy",
            "Dawson",
            "0-7414-9349-7",
            (short) 2025,
            200
        );

        when(bookService.update(any(Integer.class), any(BookRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/books/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.title").value("Dummy"))
                .andExpect(jsonPath("$.isbn").value("0-7414-9349-7"));
    }

    @Test
    @DisplayName("PATCH /books/{id} should partially update book and return 200")
    void patchBookShouldReturnOk() throws Exception {

        PatchBookRequestDTO request = new PatchBookRequestDTO(
            "Updated Dummy",
            null,
            null,
            null,
            null
        );

        BookResponseDTO response = new BookResponseDTO(
            1,
            "Updated Dummy",
            "Dawson",
            "0-7414-9349-7",
            (short) 2025,
            200
        );

        when(bookService.patch(any(Integer.class), any(PatchBookRequestDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/books/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.title").value("Updated Dummy"))
                .andExpect(jsonPath("$.isbn").value("0-7414-9349-7"));
    }

    @Test
    @DisplayName("DELETE /books/{id} should return 204")
    void deleteBookShouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).delete(1);
    }
}