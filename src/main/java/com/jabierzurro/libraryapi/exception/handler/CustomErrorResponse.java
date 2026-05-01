package com.jabierzurro.libraryapi.exception.handler;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Standard error response returned by the API.
 * Contains the error message and the timestamp of the error.
 * 
 * @author Jabier Zurro Aduriz
 */
@Getter
public class CustomErrorResponse {
    private final String message;
    private final LocalDateTime timestamp;

    public CustomErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
