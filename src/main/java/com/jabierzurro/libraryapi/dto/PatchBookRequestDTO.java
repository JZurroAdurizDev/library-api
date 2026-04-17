package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchBookRequestDTO {
    @Size(max = 200)
    private String title;

    @Size(max = 150)
    private String author;

    @Size(min = 10, max = 13)
    private String isbn;

    private Short publishedYear;

    private Integer pages;
}
