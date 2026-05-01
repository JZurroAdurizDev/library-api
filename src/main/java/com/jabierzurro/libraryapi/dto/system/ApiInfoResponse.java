package com.jabierzurro.libraryapi.dto.system;

/**
 * Data Transfer Object representing API metadata.
 *
 * <p>This record is returned by system endpoints to provide
 * basic information about the service.
 *
 * @param service     the name of the API
 * @param version     the current version of the API
 * @param description a short description of the API purpose
 * @param author      the author or maintainer of the API
 *
 * @author Jabier Zurro Aduriz
 */
public record ApiInfoResponse(
        String service,
        String version,
        String description,
        String author
    ) {
}
