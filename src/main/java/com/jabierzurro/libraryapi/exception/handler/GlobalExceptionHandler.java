package com.jabierzurro.libraryapi.exception.handler;

import com.jabierzurro.libraryapi.exception.base.ConflictException;
import com.jabierzurro.libraryapi.exception.base.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the API.
 * Converts domain exceptions into standardized HTTP responses.
 * 
 * @author Jabier Zurro Aduriz
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomErrorResponse> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse("Invalid request body. Check JSON format and date format. Dates must use yyyy-MM-dd."),
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCredentials(
            BadCredentialsException ex
    ) {
        return new ResponseEntity<>(
                new CustomErrorResponse("Invalid email or password"),
                HttpStatus.UNAUTHORIZED
        );
    }
    
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse("Access denied"),
                HttpStatus.FORBIDDEN
        );
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse("Access denied"),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new CustomErrorResponse("An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
