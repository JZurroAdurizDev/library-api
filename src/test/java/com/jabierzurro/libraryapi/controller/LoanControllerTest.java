package com.jabierzurro.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.dto.LoanResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchLoanRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateLoanRequestDTO;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import com.jabierzurro.libraryapi.service.LoanService;
import java.time.LocalDate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;
    
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;
    
    
    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules();
    
    @Test
    @DisplayName("GET /loans should return loan list")
    void getAllLoansShouldReturnOk() throws Exception {

        List<LoanResponseDTO> loans = List.of(
            new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
            )
        );

        when(loanService.getAllLoans()).thenReturn(loans);

        mockMvc.perform(get("/loans"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].loanId").value(1))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"))
            .andExpect(jsonPath("$[0].books.length()").value(1))
            .andExpect(jsonPath("$[0].books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("GET /loans/{id} should return loan when it exists")
    void getLoanByIdShouldReturnOk() throws Exception {

        LoanResponseDTO loan = new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
        );

        when(loanService.getLoanById(1)).thenReturn(loan);

        mockMvc.perform(get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.books.length()").value(1))
                .andExpect(jsonPath("$.books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("GET /loans/search should return filtered loans")
    void searchLoansShouldReturnOk() throws Exception {

        List<LoanResponseDTO> loans = List.of(
            new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
            )
        );

        when(loanService.search(1, null, null, null)).thenReturn(loans);

        mockMvc.perform(get("/loans/search")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].loanId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("POST /loans should create loan and return 201")
    void createLoanShouldReturnCreated() throws Exception {

        LoanRequestDTO request = new LoanRequestDTO(
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                List.of(1)
        );

        LoanResponseDTO response = new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
        );

        when(loanService.create(any(LoanRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/loans")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("PUT /loans/{id} should update loan and return 200")
    void updateLoanShouldReturnOk() throws Exception {

        UpdateLoanRequestDTO request = new UpdateLoanRequestDTO(
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                LoanStatus.ACTIVE
        );

        LoanResponseDTO response = new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
        );

        when(loanService.update(any(Integer.class), any(UpdateLoanRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/loans/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("PATCH /loans/{id} should partially update loan and return 200")
    void patchLoanShouldReturnOk() throws Exception {

        PatchLoanRequestDTO request = new PatchLoanRequestDTO(
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                LoanStatus.ACTIVE
        );

        LoanResponseDTO response = new LoanResponseDTO(
                1,
                1,
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                null,
                "ACTIVE",
                List.of(
                    new BookResponseDTO(
                        1,
                        "Dummy",
                        "Dawson",
                        "0-7414-9349-7",
                        (short) 2025,
                        200
                    )
                )
        );

        when(loanService.patch(any(Integer.class), any(PatchLoanRequestDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/loans/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.books[0].bookId").value(1));
    }
    
    @Test
    @DisplayName("DELETE /loans/{id} should return 204")
    void deleteLoanShouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/loans/1"))
                .andExpect(status().isNoContent());

        verify(loanService).delete(1);
    }
}
