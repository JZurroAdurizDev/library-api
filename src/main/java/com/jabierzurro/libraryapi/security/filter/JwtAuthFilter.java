package com.jabierzurro.libraryapi.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jabierzurro.libraryapi.security.model.UserDetailsImpl;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom security filter responsible for JWT-based authentication.
 *
 * <p>This filter is executed once per request and checks whether the incoming
 * HTTP request contains a valid JWT token in the {@code Authorization} header.
 *
 * <p>If a valid token is found:
 * <ul>
 *     <li>the token is validated,</li>
 *     <li>the username is extracted,</li>
 *     <li>the user details are loaded from the persistence layer,</li>
 *     <li>the authorities are obtained from the loaded user details,</li>
 *     <li>an {@link Authentication} object is created,</li>
 *     <li>the authentication is stored in the {@link SecurityContext}.</li>
 * </ul>
 *
 * <p>If no token is present, or if the header does not start with
 * {@code Bearer }, the filter simply passes control to the next element
 * in the filter chain.
 *
 * @author Jabier Zurro Aduriz
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor for injecting the JWT service and user details service.
     *
     * @param jwtService service responsible for token validation and claim extraction
     * @param userDetailsService service used to load authenticated user details
     */
    public JwtAuthFilter(
            JwtService jwtService,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Processes each HTTP request and attempts to authenticate the user
     * based on the JWT token provided in the {@code Authorization} header.
     *
     * <p>If the token is valid, the user's authentication is stored in the
     * {@link SecurityContextHolder}, allowing downstream security checks
     * to treat the request as authenticated.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param filterChain security filter chain
     * @throws ServletException if the request cannot be processed
     * @throws IOException if an input or output error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            DecodedJWT decodedJWT = jwtService.validateToken(jwtToken);
            String username = jwtService.extractUsername(decodedJWT);

            UserDetailsImpl userDetails =
                    (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}