/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"bookId", "title", "author", "isbn", "publishedYear", "pages"})
public record BookResponseDTO(
        Integer bookId, 
        String title,
        String author,
        String isbn,
        Short publishedYear,
        Integer pages) {

}
