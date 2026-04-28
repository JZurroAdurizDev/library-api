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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web layer test class for {@link BookController}.
 *
 * <p>This test suite verifies the HTTP contract exposed by the controller,
 * including request handling, response status codes and JSON response structure.
 *
 * <p>The controller is tested in isolation using {@link WebMvcTest}, while
 * all dependencies are mocked.
 *
 * <p>Security filters are disabled to focus exclusively on controller behavior.
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    /**
     * Mock MVC client used to perform HTTP requests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Object mapper used to serialize request bodies to JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mocked service layer dependency.
     */
    @MockitoBean
    private BookService bookService;

    /**
     * Mocked JWT service required for application context initialization.
     */
    @MockitoBean
    private JwtService jwtService;

    /**
     * Mocked user details service required for application context initialization.
     */
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Verifies that GET /books returns a list of books with HTTP 200 status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "USER")
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

    /**
     * Verifies that GET /books/{id} returns a book when it exists.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "USER")
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

    /**
     * Verifies that GET /books/search returns filtered results based on query parameters.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "USER")
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

    /**
     * Verifies that POST /books creates a new book and returns HTTP 201.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
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

    /**
     * Verifies that PUT /books/{id} fully updates a book and returns HTTP 200.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
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

    /**
     * Verifies that PATCH /books/{id} partially updates a book and returns HTTP 200.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
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

    /**
     * Verifies that DELETE /books/{id} removes a book and returns HTTP 204.
     *
     * <p>Also verifies that the delete operation is delegated to the service layer.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /books/{id} should return 204")
    void deleteBookShouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).delete(1);
    }
}