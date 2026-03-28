package com.jabierzurro.libraryapi.dto.system;

/**
 * Data Transfer Object representing the health status of the API.
 *
 * <p>This record is returned by the health endpoint to indicate
 * whether the API is running correctly.
 *
 * @param status a human-readable message describing the API status
 *
 * @author Jabier Zurro Aduriz
 */
public record HealthResponse(
        String status
    ) {
}
