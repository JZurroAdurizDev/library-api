package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.dto.system.ApiInfoResponse;
import com.jabierzurro.libraryapi.dto.system.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing public system endpoints.
 *
 * <p>This controller provides basic information about the API and its health status.
 * These endpoints are intended for monitoring, debugging and validation purposes.
 *
 * <ul>
 *     <li><b>GET /health</b> - Returns the current status of the API</li>
 *     <li><b>GET /, /info</b> - Returns metadata about the API</li>
 * </ul>
 *
 * <p>No authentication is required for these endpoints.
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
public class SystemController {

    /**
     * Returns the current health status of the API.
     *
     * @return a {@link ResponseEntity} containing a {@link HealthResponse}
     *         with a human-readable status message
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth() {
        return ResponseEntity.ok(new HealthResponse("This API is running right now."));
    }

    /**
     * Returns general information about the API.
     *
     * <p>This endpoint is accessible via "/", "/info".
     *
     * @return a {@link ResponseEntity} containing an {@link ApiInfoResponse}
     *         with service metadata such as name, version, description and author
     */
    @GetMapping({"", "/", "/info"})
    public ResponseEntity<ApiInfoResponse> getInfo() {
        return ResponseEntity.ok(
                new ApiInfoResponse(
                        "library-api",
                        "1.0.0",
                        "REST API for library management",
                        "Jabier Zurro Aduriz"
                )
        );
    }
}