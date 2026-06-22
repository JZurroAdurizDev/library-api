package com.jabierzurro.libraryapi.security.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface JwtCookieService {

    ResponseCookie createAuthenticationCookie(String token);

    ResponseCookie createExpiredAuthenticationCookie();

    String extractToken(HttpServletRequest request);
}