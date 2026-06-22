package com.jabierzurro.libraryapi.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class JwtCookieServiceImpl implements JwtCookieService {

    private static final String COOKIE_NAME = "access_token";

    private final long expirationMillis;

    public JwtCookieServiceImpl(
            @Value("${security.jwt.expiration}") long expirationMillis
    ) {
        this.expirationMillis = expirationMillis;
    }

    @Override
    public ResponseCookie createAuthenticationCookie(String token) {
        return ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMillis(this.expirationMillis))
                .build();
    }

    @Override
    public ResponseCookie createExpiredAuthenticationCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);

        return cookie != null
                ? cookie.getValue()
                : null;
    }
}
